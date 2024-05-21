package com.example.oneinkedoneproject.controller.chat;

import com.example.oneinkedoneproject.domain.Chat;
import com.example.oneinkedoneproject.domain.Grade;
import com.example.oneinkedoneproject.domain.PasswordQuestion;
import com.example.oneinkedoneproject.domain.User;
import com.example.oneinkedoneproject.dto.auth.TokenInfo;
import com.example.oneinkedoneproject.dto.chat.AddChatRequestDto;
import com.example.oneinkedoneproject.repository.article.ArticleRepository;
import com.example.oneinkedoneproject.repository.chat.ChatRepository;
import com.example.oneinkedoneproject.repository.follow.FollowRepository;
import com.example.oneinkedoneproject.repository.image.ImageRepository;
import com.example.oneinkedoneproject.repository.password.PasswordRepository;
import com.example.oneinkedoneproject.repository.resume.ResumeRepository;
import com.example.oneinkedoneproject.repository.user.UserRepository;
import com.example.oneinkedoneproject.service.auth.JwtTokenProvider;
import com.example.oneinkedoneproject.service.chat.ChatService;
import com.example.oneinkedoneproject.utils.GenerateIdUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class ChatIntegratedTest {
    @Autowired
    WebApplicationContext context;

    @Autowired
    private MockMvc mvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    ChatController chatController;

    @Autowired
    ChatService chatService;

    private String accessToken;

    private User user;

    private Chat sendchat;

    private User partner;

    private Chat receivechat;

    private Chat forDeleteChat;

    private PasswordQuestion passwordQuestion;
    @Autowired
    private ChatRepository chatRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordRepository passwordRepository;
    @Autowired
    private JwtTokenProvider jwtTokenProvider;
    @Autowired
    private ResumeRepository resumeRepository;
    @Autowired
    private FollowRepository followRepository;
    @Autowired
    private ImageRepository imageRepository;
    @Autowired
    private ArticleRepository articleRepository;

    @BeforeEach
    void setUp() {
        resumeRepository.deleteAll();
        chatRepository.deleteAll();
        followRepository.deleteAll();
        imageRepository.deleteAll();
        articleRepository.deleteAll();
        userRepository.deleteAll();
//        userRepository.deleteAll();
        this.mvc = MockMvcBuilders.webAppContextSetup(context).
                apply(SecurityMockMvcConfigurers.springSecurity()).build();

        this.user = userRepository.save(User.builder()
                .withdraw(false)
                .email("test@test.com")
                .password("1234")
//                .id("1")
                .realname("name")
                .grade(Grade.ROLE_BASIC)
                .passwordQuestion(
                        passwordRepository.save(PasswordQuestion.builder()
//                                .id("1")
                                .question("?")
                                .build()))
                .build());
        Authentication auth = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());

        TokenInfo tokenInfo = jwtTokenProvider.createToken(auth);
//        String accessToken = tokenInfo.getAccessToken();

        // Set the JWT token in the security context (for internal use)
        SecurityContextHolder.getContext().setAuthentication(auth);

        // Store the accessToken for use in requests
        this.accessToken = tokenInfo.getAccessToken();
//        String accessToken = tokenInfo.getAccessToken();

        partner = userRepository.save(User.builder()
                .id(GenerateIdUtils.generateUserId())
                .realname("test")
                .email("test1@test.com")
                .password("test")
                .grade(Grade.ROLE_BASIC)
                .withdraw(false)
                .passwordQuestion(
                        passwordRepository.save(PasswordQuestion.builder()
//                                .id("1")
                                .question("?")
                                .build()))
                .build());

        sendchat = chatRepository.save(Chat.builder()
                .id("1").contents("test").sendUser(user).receiverUser(partner)
                .isDeletedTo(false).isDeletedFrom(false)
                .sendAt(LocalDateTime.now()).build());

        receivechat = chatRepository.save(Chat.builder()
                .id("2").contents("test2").sendUser(partner).receiverUser(user)
                .isDeletedFrom(false).isDeletedTo(false)
                .sendAt(LocalDateTime.now().minusMinutes(5)).build()) ;

        forDeleteChat = chatRepository.save(Chat.builder()
                .id("3").contents("test3").sendUser(partner).receiverUser(user)
                .isDeletedFrom(true).isDeletedTo(true)
                .sendAt(LocalDateTime.now().minusMinutes(5)).build());
    }

    @AfterEach
    void  afterSetUp() {
        resumeRepository.deleteAll();
        chatRepository.deleteAll();
        followRepository.deleteAll();
        imageRepository.deleteAll();
        articleRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void createChat() throws Exception {
        AddChatRequestDto addChatRequestDto = AddChatRequestDto.builder()
                .partnerEmail("test1@test.com").contents("test").build();
        String json = objectMapper.writeValueAsString(addChatRequestDto);

        ResultActions resultActions = mvc.perform(post("/api/createChat")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.contents").value("test"));
    }

    @Test
    void updateIsDeletedTest() throws Exception {
//        String json = objectMapper.writeValueAsString("test1@test.com");
        String json = "test1@test.com";
        ResultActions resultActions = mvc.perform(put("/api/updateIsDeleted")
                        .header("Authorization", "Bearer " + accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("partnerEmail", partner.getEmail()))
                .andDo(print())
                .andExpect(jsonPath("$[0].isDeletedFrom").value(true));
    }

    @Test
    void deleteChatTest() throws Exception {
        String json = "test1@test.com";
        ResultActions resultActions = mvc.perform(delete("/api/deleteChat")
                        .header("Authorization", "Bearer " + accessToken)
                        .content(json))
                .andExpect(status().isOk())
                .andDo(print());

        boolean exists = chatRepository.existsById("3");
        assertThat(exists).isFalse();
    }
}
