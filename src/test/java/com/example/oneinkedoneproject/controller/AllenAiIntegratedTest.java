package com.example.oneinkedoneproject.controller;

import com.example.oneinkedoneproject.domain.Grade;
import com.example.oneinkedoneproject.domain.PasswordQuestion;
import com.example.oneinkedoneproject.domain.User;
import com.example.oneinkedoneproject.dto.auth.TokenInfo;
import com.example.oneinkedoneproject.repository.password.PasswordRepository;
import com.example.oneinkedoneproject.repository.user.UserRepository;
import com.example.oneinkedoneproject.service.auth.JwtTokenProvider;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
public class AllenAiIntegratedTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    ObjectMapper objectMapper;

    private String accessToken;

    private User user;

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordRepository passwordRepository;

    @Autowired
    JwtTokenProvider jwtTokenProvider;

    @BeforeEach
    void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).
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
    }

    @Test
    void queryAiTest() throws Exception {
        ResultActions resultActions = mockMvc.perform(get("/api/resumeReview")
                        .param("client_id", "8c9cad6a-45a6-4129-a938-0db6b017899d")
                        .param("content", "날씨 알려줘")
                        .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.action.name").value("search_weather"));

    }
}
