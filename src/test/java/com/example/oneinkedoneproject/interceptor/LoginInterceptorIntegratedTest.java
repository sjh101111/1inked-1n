package com.example.oneinkedoneproject.interceptor;

import com.example.oneinkedoneproject.domain.Grade;
import com.example.oneinkedoneproject.domain.PasswordQuestion;
import com.example.oneinkedoneproject.domain.User;
import com.example.oneinkedoneproject.repository.user.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class LoginInterceptorIntegratedTest {
    private User user;
    private UserRepository userRepository;
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    private MockHttpServletRequest request;
    private MockHttpServletResponse response;
    @BeforeEach
    void setup(){
        bCryptPasswordEncoder = new BCryptPasswordEncoder();
        User user = User.builder()
                .id("USER_ID123")
                .realname("realname")
                .email("email@email.com")
                .password(bCryptPasswordEncoder.encode("password"))
                .passwordQuestion(new PasswordQuestion("123", "question"))
                .withdraw(true)
                .build();
        userRepository.save(user);

        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
    }


    @Test
    @DisplayName("정상 로그인 작동 확인")
    void correctLogin() {


    }

}
