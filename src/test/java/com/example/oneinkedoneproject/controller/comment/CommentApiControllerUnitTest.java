package com.example.oneinkedoneproject.controller.comment;

import com.example.oneinkedoneproject.domain.Article;
import com.example.oneinkedoneproject.domain.Comment;
import com.example.oneinkedoneproject.domain.Grade;
import com.example.oneinkedoneproject.domain.PasswordQuestion;
import com.example.oneinkedoneproject.domain.User;
import com.example.oneinkedoneproject.dto.comment.AddCommentRequestDto;
import com.example.oneinkedoneproject.service.comment.CommentService;
import com.example.oneinkedoneproject.utils.GenerateIdUtils;
import com.google.gson.Gson;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class CommentApiControllerUnitTest {
    @Mock
    CommentService commentService;

    @InjectMocks
    CommentApiController commentController;

    private MockMvc mockMvc;
    private Gson gson;
    private static User mockedUser;
    private static Article article;
    private final String url = "/api/comment/1";

    @BeforeEach
    public void init() {
        mockMvc = MockMvcBuilders.standaloneSetup(commentController).build();
        gson = new Gson();

        mockedUser = User.builder()
                .withdraw(false)
                .email("test@test.com")
                .password("1234")
                .id("1")
                .realname("name")
                .grade(Grade.ROLE_BASIC)
                .passwordQuestion(PasswordQuestion.builder()
                        .id("1")
                        .question("?")
                        .build())
                .build();

        article = Article.builder()
                .id("1")
                .contents("게시글 내용")
                .user(mockedUser)
                .build();
    }

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

    @Test
    @DisplayName("댓글 생성 실패 - invalid articleId")
    
    public void testInvalidArticleId() throws Exception {
        // given

        // when
        ResultActions resultActions = mockMvc.perform(
                post(url).with(user(mockedUser))
                        .content(gson.toJson("댓글 내용"))
                        .contentType(MediaType.APPLICATION_JSON)
                        
        );

        // then
        resultActions.andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("댓글 생성")
    public void createComment() throws Exception {
        // given
        Comment comment = buildComment("댓글 내용");

        doReturn(comment).when(commentService).save(any(), any(), any());

        // when
        ResultActions resultActions = mockMvc.perform(
                post(url).with(user(mockedUser))
                        
                        .content(gson.toJson(new AddCommentRequestDto("댓글 내용", null)))
                        .contentType(MediaType.APPLICATION_JSON)
        );

        // then
        resultActions.andExpect(status().isCreated())
                .andDo(print());
    }

    @Test
    @DisplayName("대댓글 조회 테스트")
    void createReply() throws Exception {
        // given
        Comment rootComment = buildComment("root");
        Comment childComment1 = buildComment("child1", rootComment);
        Comment childComment2 = buildComment("child2", rootComment);
        Comment childComment3 = buildComment("child3", childComment1);

        rootComment.getReplyList().add(childComment1);
        rootComment.getReplyList().add(childComment2);
        childComment1.getReplyList().add(childComment3);
        List<Comment> commentList = new ArrayList<>();
        commentList.add(rootComment);
        doReturn(commentList).when(commentService).getComments("1");

        // when
        ResultActions resultActions = mockMvc.perform(get(url).with(user(mockedUser)));

        // then
        resultActions.andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("댓글 수정")
//    @WithMockCustomUser
    void updateComment() throws Exception {
        // given
        Comment comment = buildComment("바뀐 댓글 내용");
        doReturn(comment).when(commentService).updateComment(any(), any());

        // when
        ResultActions resultActions = mockMvc.perform(
                patch(url).with(user(mockedUser))
                        .content(gson.toJson("바뀐 댓글 내용"))
                        .contentType(MediaType.APPLICATION_JSON)
        );

        // then
        resultActions.andExpect(status().isOk())
                .andDo(print());
    }
}
