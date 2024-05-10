package com.example.oneinkedoneproject.service.user;

import com.example.oneinkedoneproject.domain.Grade;
import com.example.oneinkedoneproject.domain.PasswordQuestion;
import com.example.oneinkedoneproject.domain.User;
import com.example.oneinkedoneproject.dto.user.LoginUserRequestDto;
import com.example.oneinkedoneproject.repository.password.PasswordRepository;
import com.example.oneinkedoneproject.repository.user.UserRepository;
import com.example.oneinkedoneproject.utils.GenerateIdUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class LoginServiceUnitTest {

    private PasswordQuestion pwdQuestion;


    private LoginService loginService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private PasswordRepository passwordRepository;

    @Autowired
    private UserRepository userRepository;
    @BeforeEach
    void init(){
        encoder = new BCryptPasswordEncoder();
        pwdQuestion = new PasswordQuestion("1", "질문");
        passwordRepository.save(pwdQuestion);
        loginService = new LoginService(authenticationManager);
    }

    @Test
    @DisplayName("로그인에 성공했을 경우")
    void loginSecessfulTest(){
        String email = "test123@naver.com";
        User user = User.builder()
                .id(GenerateIdUtils.generateUserId())
                .realname("익명1")
                .passwordQuestion(pwdQuestion)
                .passwordAnswer("aw")
                .email(email)
                .password(encoder.encode("1aw9!wWem23"))
                .withdraw(false)
                .grade(Grade.ROLE_GOLD)
                .build();
        userRepository.deleteAll();
        userRepository.save(user);

        //Authentication auth = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken("test123@naver.com", "1aw9!wWem23"));
        Authentication auth = loginService.login(new LoginUserRequestDto(email, "1aw9!wWem23"));

        assertNotEquals(auth, null);
        assertTrue(auth.isAuthenticated());
    }
}
