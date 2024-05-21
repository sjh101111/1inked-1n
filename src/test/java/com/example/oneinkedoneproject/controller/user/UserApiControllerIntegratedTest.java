package com.example.oneinkedoneproject.controller.user;


import com.example.oneinkedoneproject.domain.Grade;
import com.example.oneinkedoneproject.domain.PasswordQuestion;
import com.example.oneinkedoneproject.domain.User;
import com.example.oneinkedoneproject.dto.auth.TokenInfo;
import com.example.oneinkedoneproject.dto.user.ChangePasswordRequestDto;
import com.example.oneinkedoneproject.dto.user.SignupUserRequestDto;
import com.example.oneinkedoneproject.dto.user.WithdrawUserRequestDto;
import com.example.oneinkedoneproject.repository.password.PasswordRepository;
import com.example.oneinkedoneproject.repository.user.UserRepository;
import com.example.oneinkedoneproject.service.auth.JwtTokenProvider;
import com.example.oneinkedoneproject.utils.GenerateIdUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Transactional
public class UserApiControllerIntegratedTest {
    @Autowired
    private WebApplicationContext context;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordRepository passwordRepository;

    @Autowired
    MockMvc mockMvc;

//    @MockBean
//    JwtAuthenticationFilter jwtAuthenticationFilter;
//
//    @MockBean
//    JwtRefreshTokenFilter jwtRefreshTokenFilter;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private PasswordEncoder encoder;


    private ObjectMapper om;
    private PasswordQuestion mockPwdQuestion;

    @BeforeEach
    void init() {
        om = new ObjectMapper();
        mockPwdQuestion = passwordRepository.save(PasswordQuestion.builder()
                .id("1")
                .question("?")
                .build());
        mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .apply(SecurityMockMvcConfigurers.springSecurity()).build();
    }

    @Test
    @DisplayName("유저 프로필 조회")
    void findUserTest() throws Exception {
        //given
        String email = "test@naver.com";
        User user = User.builder()
                .id(GenerateIdUtils.generateUserId())
                .realname("익명")
                .passwordQuestion(mockPwdQuestion)
                .passwordAnswer("aw")
                .email(email)
                .password("1aw9!wWem23")
                .withdraw(false)
                .grade(Grade.ROLE_BASIC)
                .build();
        userRepository.save(user);

        Authentication auth = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());

        TokenInfo tokenInfo = jwtTokenProvider.createToken(auth);

        SecurityContextHolder.getContext().setAuthentication(auth);

        String accessToken = tokenInfo.getAccessToken();

        //when
        ResultActions actions = mockMvc.perform(get("/api/user?email=" + email)
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + accessToken));


        //then
        actions.andExpect(status().isOk())
                .andExpect(jsonPath("email").value(email));
    }


    @Test
    @DisplayName("유저 프로필 저장 API 통합테스트")
    void saveProfileUserTest() throws Exception {
        //given
        String email = "test@naver.com";
        String identity = "태북";
        String location = "서울";
        String description = "울라리";
        User user = User.builder()
                .id(GenerateIdUtils.generateUserId())
                .realname("익명")
                .passwordQuestion(mockPwdQuestion)
                .passwordAnswer("aw")
                .email(email)
                .password("1aw9!wWem23")
                .grade(Grade.ROLE_BASIC)
                .withdraw(false)
                .build();
        userRepository.save(user);

        MockMultipartFile file = new MockMultipartFile("file", "image.png", "image/png", "<<image>>".getBytes());

        Authentication auth = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());

        TokenInfo tokenInfo = jwtTokenProvider.createToken(auth);

        SecurityContextHolder.getContext().setAuthentication(auth);

        String accessToken = tokenInfo.getAccessToken();

        //when
        ResultActions actions = mockMvc.perform(multipart("/api/profile")
                .file(file)
                .header("Authorization", "Bearer " + accessToken)
                .param("email", email)
                .param("identity", identity)
                .param("location", location)
                .param("description", description));

        //then
        actions.andExpect(status().isOk());
    }

    @Test
    @DisplayName("유저 비밀번호 변경 API 통합 테스트")
    void changePasswordUserTest() throws Exception {
        //given
        String email = "test@naver.com";
        String newPassword = "qr1!42fwwamFqw";
        String passwordQuestionAnswer = "숲튽훈";
        User user = User.builder()
                .id(GenerateIdUtils.generateUserId())
                .realname("익명")
                .passwordQuestion(mockPwdQuestion)
                .passwordAnswer(passwordQuestionAnswer)
                .email(email)
                .grade(Grade.ROLE_BASIC)
                .password("1aw9!wWem23")
                .withdraw(false)
                .build();
        userRepository.save(user);
        ChangePasswordRequestDto request = new ChangePasswordRequestDto(email, mockPwdQuestion.getId(), passwordQuestionAnswer, newPassword);
        Authentication auth = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());

        TokenInfo tokenInfo = jwtTokenProvider.createToken(auth);

        SecurityContextHolder.getContext().setAuthentication(auth);

        String accessToken = tokenInfo.getAccessToken();
        //when
        ResultActions actions = mockMvc.perform(post("/api/password")
                .header("Authorization", "Bearer " + accessToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(request)));

        //then
        actions.andExpect(status().isOk());
    }

    @Test
    @DisplayName("회원 탈퇴 API 통합 테스트")
    void withdrawUserTest() throws Exception {
        //given
        String email = "test@naver.com";
        String password = "1aw9!wWem23";
        boolean isWithdraw = true;
        User user = User.builder()
                .id(GenerateIdUtils.generateUserId())
                .realname("익명")
                .passwordQuestion(mockPwdQuestion)
                .passwordAnswer("슾튽훈")
                .email(email)
                .password(encoder.encode(password))
                .grade(Grade.ROLE_BASIC)
                .withdraw(false)
                .build();
        WithdrawUserRequestDto request = new WithdrawUserRequestDto(password, isWithdraw);
        userRepository.save(user);

        Authentication auth = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());

        TokenInfo tokenInfo = jwtTokenProvider.createToken(auth);

        SecurityContextHolder.getContext().setAuthentication(auth);

        String accessToken = tokenInfo.getAccessToken();

        //when
        ResultActions actions = mockMvc.perform(post("/api/withdraw")
                .header("Authorization", "Bearer " + accessToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(request)));

        //then
        actions.andExpect(status().isOk());
    }

    @Test
    @DisplayName("회원가입 API 통합테스트")
    void signupUserTest() throws Exception {
        //given
        String realName = "이태희";
        String email = "dlxogml11235@naver.com";
        String password = "1q2!eqwRq13";

        SignupUserRequestDto request = new SignupUserRequestDto(realName, email, password, mockPwdQuestion.getId(), mockPwdQuestion.getQuestion());
        //when
        ResultActions actions = mockMvc.perform(put("/api/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(request)));

        //then
        actions.andExpect(status().isCreated()).andDo(print());
    }

    @Test
    @DisplayName("유저 사진 업로드 API 통합 테스트")
    void uploadImageUserTest() throws Exception {
        //given
        String email = "test@naver.com";
        User user = User.builder()
                .id(GenerateIdUtils.generateUserId())
                .realname("익명")
                .passwordQuestion(mockPwdQuestion)
                .passwordAnswer("슾튽훈")
                .email(email)
                .password("123")
                .withdraw(false)
                .grade(Grade.ROLE_BASIC)
                .build();
        MockMultipartFile file = new MockMultipartFile("file", "image.png", "image/png", "<<image>>".getBytes());
        userRepository.save(user);

        Authentication auth = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());

        TokenInfo tokenInfo = jwtTokenProvider.createToken(auth);

        SecurityContextHolder.getContext().setAuthentication(auth);

        String accessToken = tokenInfo.getAccessToken();

        //when
        ResultActions actions = mockMvc.perform(multipart("/api/user/image")
                .file(file)
                .header("Authorization", "Bearer " + accessToken)
                .param("email", email));

        //then
        actions.andExpect(status().isOk());
    }
}
