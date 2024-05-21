package com.example.oneinkedoneproject.controller.comment;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.oneinkedoneproject.dto.auth.TokenInfo;
import com.example.oneinkedoneproject.service.auth.JwtTokenProvider;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.example.oneinkedoneproject.domain.Article;
import com.example.oneinkedoneproject.domain.Comment;
import com.example.oneinkedoneproject.domain.Grade;
import com.example.oneinkedoneproject.domain.PasswordQuestion;
import com.example.oneinkedoneproject.domain.User;
import com.example.oneinkedoneproject.dto.comment.AddCommentRequestDto;
import com.example.oneinkedoneproject.dto.comment.UpdateCommentRequestDto;
import com.example.oneinkedoneproject.repository.article.ArticleRepository;
import com.example.oneinkedoneproject.repository.comment.CommentRepository;
import com.example.oneinkedoneproject.repository.password.PasswordRepository;
import com.example.oneinkedoneproject.repository.user.UserRepository;
import com.example.oneinkedoneproject.service.comment.CommentService;
import com.example.oneinkedoneproject.utils.GenerateIdUtils;
import com.google.gson.Gson;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class CommentApiControllerIntegratedTest {
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private PasswordRepository passwordRepository;
    
    @Autowired
    private ArticleRepository articleRepository;
    
    @Autowired
    private CommentService commentService;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;


    @Autowired
    MockMvc mockMvc;
    
    private User mockedUser;
    private Article article;
    private Gson gson;
    private String accessToken;


    private Comment buildComment(String comments, Comment parent) {
        return Comment.builder()
                .user(mockedUser)
                .parent(parent)
                .article(article)
                .comments(comments)
                .id(GenerateIdUtils.generateCommentId())
                .build();
    }

    private Comment buildComment(String comments) {
        return Comment.builder()
                .user(mockedUser)
                .article(article)
                .comments(comments)
                .id(GenerateIdUtils.generateCommentId())
                .build();
    }
    
    @BeforeEach
    public void setUp() throws Exception {
    	System.out.println("BeforeEach");
    	
    	gson = new Gson();
    	PasswordQuestion pq = passwordRepository.save(PasswordQuestion.builder()
        .id("1")
        .question("?")
        .build());
    	mockedUser = userRepository.save(User.builder()
                .withdraw(false)
                .email("test@test.com")
                .password("1234")
                .id("1")
                .realname("name")
                .grade(Grade.ROLE_BASIC)
                .passwordQuestion(pq)
                .build());

        Authentication auth = new UsernamePasswordAuthenticationToken(mockedUser, null, mockedUser.getAuthorities());

        TokenInfo tokenInfo = jwtTokenProvider.createToken(auth);
        String accessToken = tokenInfo.getAccessToken();

        // Set the JWT token in the security context (for internal use)
        SecurityContextHolder.getContext().setAuthentication(auth);

        // Store the accessToken for use in requests
        this.accessToken = accessToken;

        article = articleRepository.save(Article.builder()
                .id("1")
                .contents("게시글 내용")
                .user(mockedUser)
                .build());
        
        System.out.println("End BeforeEach");
    }

    @AfterEach
    public void deleteData() throws Exception {
        System.out.println("AfterEach");

        articleRepository.deleteById("1");
        userRepository.deleteById("1");
        passwordRepository.deleteById("1");
        
        System.out.println("End AfterEach");
    }

    @Test
    @DisplayName("댓글 생성")
    void testCreateComment() throws Exception {
        // given
        AddCommentRequestDto requestDto = new AddCommentRequestDto("댓글 내용", null);

        //when
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.post("/api/comment/1")
                .header("Authorization", "Bearer " + accessToken)
        		.contentType(MediaType.APPLICATION_JSON)
        		.content(gson.toJson(requestDto))
        		)
        		// then
        		.andExpect(status().isCreated()).andDo(print());
    }
    
    @Test
    @DisplayName("댓글 조회")
    void readComment() throws Exception{
    	//given
    	commentService.save(mockedUser, "1", new AddCommentRequestDto("댓글 내용", null));
    	//when
    	ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.get("/api/comment/1")
                .header("Authorization", "Bearer " + accessToken)
    			);
    	
    	//then
    	resultActions.andExpect(status().isOk()).andDo(print());
    }
    
    @Test
    @DisplayName("대댓글 생성")
    void testCreateReply() throws Exception {
    	 //given
    	 Comment saved = commentService.save(mockedUser, "1", new AddCommentRequestDto("댓글 내용", null));
    	 
    	 //when
        mockMvc.perform(MockMvcRequestBuilders.post("/api/comment/1")
                        .header("Authorization", "Bearer " + accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(new AddCommentRequestDto("Root 댓글", saved.getId())))
                )
                //then
                .andExpect(status().isCreated()).andDo(print());
    }
    
    @Test
    @DisplayName("대댓글 조회")
    void testReadReply() throws Exception {
    	// given
    	Comment root = commentService.save(mockedUser, "1", new AddCommentRequestDto("ROOT", null));
    	Comment r_c1 = commentService.save(mockedUser, "1", new AddCommentRequestDto("ROOT_C1", root.getId()));
    	Comment r_c2 = commentService.save(mockedUser, "1", new AddCommentRequestDto("ROOT_C2", root.getId()));
    	Comment r_c2_c1 = commentService.save(mockedUser, "1", new AddCommentRequestDto("ROOT_C2_C1", r_c2.getId()));
    	
    	System.out.println("WHEN");
    	//when
        mockMvc.perform(MockMvcRequestBuilders.get("/api/comment/1")
                        .header("Authorization", "Bearer " + accessToken)
                )
                //then
                .andExpect(status().isOk())

                .andDo(print());
    	
    }
    @Test
    @DisplayName("댓글 업데이트")
    void testUpdate() throws Exception{
    	//given
   	 Comment saved = commentService.save(mockedUser, "1", new AddCommentRequestDto("댓글 내용", null));
   	 
   	 //when
        mockMvc.perform(MockMvcRequestBuilders.patch("/api/comment/" + saved.getId())
                        .header("Authorization", "Bearer " + accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(new UpdateCommentRequestDto("바뀐 내용")))
                )
                //then
                .andExpect(status().isOk()).andDo(print());
    }
    
    @Test
    @DisplayName("댓글 삭제")
    void testDelete() throws Exception{
    	//given
   	 Comment saved = commentService.save(mockedUser, "1", new AddCommentRequestDto("댓글 내용", null));
   	 
   	 //when
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/comment/" + saved.getId())
                        .header("Authorization", "Bearer " + accessToken)
                )
                //then
                .andExpect(status().isOk()).andDo(print());
    }
}
