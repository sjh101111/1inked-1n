package com.example.oneinkedoneproject.controller;

import com.example.oneinkedoneproject.filter.JwtAuthenticationFilter;
import com.example.oneinkedoneproject.filter.JwtRefreshTokenFilter;
import com.example.oneinkedoneproject.repository.password.PasswordRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Transactional
public class PasswordQuestionControllerIntegratedTest {
    @Autowired
    private WebApplicationContext context;

    @Autowired
    MockMvc mockMvc;

    @MockBean
    JwtAuthenticationFilter jwtAuthenticationFilter;

    @MockBean
    JwtRefreshTokenFilter jwtRefreshTokenFilter;

    private ObjectMapper om;

    @BeforeEach
    void init(){
        om = new ObjectMapper();
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @Test
    @DisplayName("패스워드 조회 API 통합 테스트")
    void findPasswordQuestionTest() throws Exception{
        //given

        //when
        ResultActions actions = mockMvc.perform(get("/api/passwordquestion"));

        //then
        actions.andExpect(status().isOk()).andDo(print());
    }
}
