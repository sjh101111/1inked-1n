package com.example.oneinkedoneproject.controller.user;


import com.example.oneinkedoneproject.domain.PasswordQuestion;
import com.example.oneinkedoneproject.domain.User;
import com.example.oneinkedoneproject.dto.user.*;
import com.example.oneinkedoneproject.service.user.UserService;
import com.example.oneinkedoneproject.utils.GenerateIdUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.doReturn;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class UserControllerUnitTest {
    @InjectMocks
    private UserController userController;

    @Mock
    private UserService userService;

    MockMvc mockMvc;

    private ObjectMapper om;

    private PasswordQuestion pwdQuestion;

    @BeforeEach
    void init() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
        pwdQuestion = new PasswordQuestion("123", "123");
        om = new ObjectMapper();
    }

    @Test
    @DisplayName("유저 프로필 조회 API 호출")
    void findUserTest() throws Exception {
        //given
        String email = "test@naver.com";
        String realName = "익명";
        FindUserRequestDto request = new FindUserRequestDto(email);
        User user = User.builder()
                .id(GenerateIdUtils.generateUserId())
                .realname(realName)
                .passwordQuestion(pwdQuestion)
                .passwordAnswer("aw")
                .email(email)
                .password("temp")
                .withdraw(false)
                .build();
        //when
        doReturn(user)
                .when(userService)
                .findUser(any(FindUserRequestDto.class));


        ResultActions actions = mockMvc.perform(get("/api/user")
                .contentType(MediaType.APPLICATION_JSON));

        //then
        actions.andExpect(status().isOk())
                .andExpect(jsonPath("realName").value(realName));
    }

    @Test
    @DisplayName("다른 유저 프로필 조회 API 호출")
    void findAnotherUserTest() throws Exception {
        String email = "test@naver.com";
        String realName = "익명";
        FindUserRequestDto request = new FindUserRequestDto(email);
        User user = User.builder()
                .id(GenerateIdUtils.generateUserId())
                .realname(realName)
                .passwordQuestion(pwdQuestion)
                .passwordAnswer("aw")
                .email(email)
                .password("temp")
                .withdraw(false)
                .build();
        //when
        doReturn(user)
                .when(userService)
                .findUser(any(FindUserRequestDto.class));


        ResultActions actions = mockMvc.perform(get("/api/user/" + email)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print());

        //then
        actions.andExpect(status().isOk())
                .andExpect(jsonPath("realName").value(realName));
    }

    @Test
    @DisplayName("유저 프로필 저장 API 호출")
    void saveUserProfileTest() throws Exception {
        //given
        String email = "test@naver.com";
        String identity = "test";
        String location = "서울";
        String description = "테스트 설명";
        MockMultipartFile file = new MockMultipartFile("file",
                "test.png",
                "image/png",
                "test".getBytes(StandardCharsets.UTF_8));

        User user = User.builder()
                .id(GenerateIdUtils.generateUserId())
                .realname("익명")
                .passwordQuestion(pwdQuestion)
                .passwordAnswer("aw")
                .email(email)
                .password("aweoim")
                .withdraw(false)
                .build();

        //when
        doReturn(user)
                .when(userService)
                .saveProfile(any(SaveProfileRequestDto.class), any(User.class));


        ResultActions actions = mockMvc.perform(multipart("/api/profile")
                .file(file)
                .param("email", email)
                .param("identity", identity)
                .param("location", location)
                .param("description", description));
        //then
        actions.andExpect(status().isOk());
    }

    @Test
    @DisplayName("유저 프로필 저장 API 실패 호출")
    void saveUserProfileFailTest() throws Exception {
        //given
        String email = "test@naver.com";
        String identity = "test";
        String location = "서울";
        String description = "테스트 설명";

        MockMultipartFile file = new MockMultipartFile("file",
                "test.png",
                "image/png",
                "test".getBytes(StandardCharsets.UTF_8));

        //when
        doReturn(null)
                .when(userService)
                .saveProfile(any(SaveProfileRequestDto.class), any(User.class));


        ResultActions actions = mockMvc.perform(multipart("/api/profile")
                .file(file)
                .param("email", email)
                .param("identity", identity)
                .param("location", location)
                .param("description", description));
        //then
        actions.andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("유저 비밀번호 변경 API 테스트")
    void changePasswordTest() throws Exception {
        //given
        ChangePasswordRequestDto request = new ChangePasswordRequestDto("test@naver.com", pwdQuestion.getId(), pwdQuestion.getQuestion(), "1q2w3!rRaw1");
        User user = User.builder()
                .id(GenerateIdUtils.generateUserId())
                .realname("익명")
                .passwordQuestion(pwdQuestion)
                .passwordAnswer("aw")
                .email("emfawoeim")
                .password("aweoim")
                .withdraw(false)
                .build();
        String requestString = om.writeValueAsString(request);
        //when
        doReturn(user)
                .when(userService)
                .changePassword(any(ChangePasswordRequestDto.class));
        ResultActions actions = mockMvc.perform(post("/api/password")
                .content(requestString)
                .contentType(MediaType.APPLICATION_JSON));
        //then
        actions.andExpect(status().isOk());
    }

    @Test
    @DisplayName("유저 비밀번호 변경 API 실패 테스트")
    void changePasswordFailTest() throws Exception {
        //given
        ChangePasswordRequestDto request = new ChangePasswordRequestDto("test@naver.com", pwdQuestion.getId(), pwdQuestion.getQuestion(), "1q2w3!rRaw1");
        User user = User.builder()
                .id(GenerateIdUtils.generateUserId())
                .realname("익명")
                .passwordQuestion(pwdQuestion)
                .passwordAnswer("aw")
                .email("emfawoeim")
                .password("aweoim")
                .withdraw(false)
                .build();
        String requestString = om.writeValueAsString(request);
        //when
        doReturn(null)
                .when(userService)
                .changePassword(any(ChangePasswordRequestDto.class));
        ResultActions actions = mockMvc.perform(post("/api/password")
                .content(requestString)
                .contentType(MediaType.APPLICATION_JSON));
        //then
        actions.andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("회원탈퇴 API 성공 테스트")
    void withdrawSuccessTest() throws Exception {
        //given
        WithdrawUserRequestDto request = new WithdrawUserRequestDto("", true);
        User user = User.builder()
                .id(GenerateIdUtils.generateUserId())
                .realname("익명")
                .passwordQuestion(pwdQuestion)
                .passwordAnswer("aw")
                .email("emfawoeim")
                .password("aweoim")
                .withdraw(false)
                .build();
        String requestString = om.writeValueAsString(request);

        //when
        doReturn(user)
                .when(userService)
                .withDraw(any(WithdrawUserRequestDto.class), any(User.class));

        ResultActions actions = mockMvc.perform(post("/api/withdraw")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestString));
        //then
        actions.andExpect(status().isOk());
    }

    @Test
    @DisplayName("회원탈퇴 API 실패 테스트")
    void withdrawUserFailTest() throws Exception {
        //given
        WithdrawUserRequestDto request = new WithdrawUserRequestDto("", true);
        String requestString = om.writeValueAsString(request);

        //when
        doReturn(null)
                .when(userService)
                .withDraw(any(WithdrawUserRequestDto.class), any(User.class));

        ResultActions actions = mockMvc.perform(post("/api/withdraw")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestString));
        //then
        actions.andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("회원가입 API 성공 테스트")
    void signupUserTest() throws Exception {
        //given
        SignupUserRequestDto request = new SignupUserRequestDto("", "", "", "", "");
        User user = User.builder()
                .id(GenerateIdUtils.generateUserId())
                .realname("익명")
                .passwordQuestion(pwdQuestion)
                .passwordAnswer("aw")
                .email("emfawoeim")
                .password("aweoim")
                .withdraw(false)
                .build();
        String requestString = om.writeValueAsString(request);

        //when
        doReturn(user)
                .when(userService)
                .signUp(any(SignupUserRequestDto.class));

        ResultActions actions = mockMvc.perform(put("/api/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestString));

        //then
        actions.andExpect(status().isCreated());
    }

    @Test
    @DisplayName("회원가입 API 실패 테스트")
    void signupUserFailTest() throws Exception {
        //given
        SignupUserRequestDto request = new SignupUserRequestDto("", "", "", "", "");

        String requestString = om.writeValueAsString(request);

        //when
        doReturn(null)
                .when(userService)
                .signUp(any(SignupUserRequestDto.class));

        ResultActions actions = mockMvc.perform(put("/api/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestString));

        //then
        actions.andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("유저 사진 업로드 성공 테스트")
    void uploadImageSuccessTest() throws Exception {
        //given
        String email = "test@naver.com";
        MockMultipartFile file = new MockMultipartFile("file", "test.png", "image/png", "image png".getBytes(StandardCharsets.UTF_8));
        User user = User.builder()
                .id(GenerateIdUtils.generateUserId())
                .realname("익명")
                .passwordQuestion(pwdQuestion)
                .passwordAnswer("aw")
                .email("test@naver.com")
                .password("aweoim")
                .withdraw(false)
                .build();

        //when
        doReturn(user)
                .when(userService)
                .uploadImage(any(UploadUserImageRequestDto.class), any(User.class));

        ResultActions actions = mockMvc.perform(multipart("/api/user/image")
                .file(file)
                .param("email", email));
        //then
        actions.andExpect(status().isOk());
    }

    @Test
    @DisplayName("유저 사진 업로드 실패 테스트")
    void uploadImageFailTest() throws Exception {
        //given
        String email = "test@naver.com";
        MockMultipartFile file = new MockMultipartFile("file", "test.png", "image/png", "image png".getBytes(StandardCharsets.UTF_8));

        //when
        doReturn(null)
                .when(userService)
                .uploadImage(any(UploadUserImageRequestDto.class), any(User.class));

        ResultActions actions = mockMvc.perform(multipart("/api/user/image")
                .file(file)
                .param("email", email));
        //then
        actions.andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("키워드로 유저 정보 조회")
    void searchUsers() throws Exception {
        String email = "test@naver.com";
        String realName = "익명";
        User user = User.builder()
                .id(GenerateIdUtils.generateUserId())
                .realname(realName)
                .passwordQuestion(pwdQuestion)
                .passwordAnswer("aw")
                .email(email)
                .password("temp")
                .withdraw(false)
                .build();

        List<FindUserResponseDto> list = new ArrayList<>();
        list.add(new FindUserResponseDto(user));

        doReturn(list).when(userService).searchUsers(any(String.class),any(String.class));

        ResultActions resultActions = mockMvc.perform(get("/api/user/search")
                        .param("keyword", "testKeyWord"))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$[0].realName").value(realName));
    }
}
