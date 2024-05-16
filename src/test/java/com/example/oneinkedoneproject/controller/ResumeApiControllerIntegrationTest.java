package com.example.oneinkedoneproject.controller;

import com.example.oneinkedoneproject.domain.*;
import com.example.oneinkedoneproject.dto.comment.UpdateCommentRequestDto;
import com.example.oneinkedoneproject.dto.auth.TokenInfo;
import com.example.oneinkedoneproject.dto.resume.AddResumeRequestDto;
import com.example.oneinkedoneproject.dto.resume.UpdateResumeRequestDto;
import com.example.oneinkedoneproject.repository.password.PasswordRepository;
import com.example.oneinkedoneproject.repository.resume.ResumeRepository;
import com.example.oneinkedoneproject.repository.user.UserRepository;
import com.example.oneinkedoneproject.service.auth.JwtTokenProvider;
import com.example.oneinkedoneproject.service.resume.ResumeService;
import com.example.oneinkedoneproject.utils.GenerateIdUtils;
import com.google.gson.Gson;
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

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class ResumeApiControllerIntegrationTest {
    @Autowired
    private PasswordRepository passwordRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private ResumeRepository resumeRepository;

    @Autowired
    private ResumeService resumeService;

    @Autowired
    MockMvc mockMvc;

    private User mockedUser;
    private Gson gson;
    private String accessToken;


    private Resume buildResume() {
        return Resume.builder()
                .id(GenerateIdUtils.generateResumeId())
                .contents("Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum.")
                .user(mockedUser)
                .build();
    }

    @BeforeEach
    void setUp() {
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
    }

    @AfterEach
    void deleteData() {
        resumeRepository.deleteAll();
        userRepository.deleteById("1");
        passwordRepository.deleteById("1");
    }

    @Test
    @DisplayName("이력서 생성")
    void saveResume() throws Exception {
        // given
        AddResumeRequestDto addResumeRequestDto = new AddResumeRequestDto("Lorem Ipsum");

        // when
        mockMvc.perform(post("/api/resume")
                        .header("Authorization", "Bearer " + accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(addResumeRequestDto))
                )
                // then
                .andExpect(status().isCreated()).andDo(print())
        ;
    }

    @Test
    @DisplayName("이력서 조회")
    void readResume() throws Exception {
        // given
        Resume resume = buildResume();
        resumeRepository.save(resume);

        // when
        mockMvc.perform(get("/api/resume/{resumeId}", resume.getId())
                        .with(user(mockedUser))
                        .header("Authorization", "Bearer " + accessToken)
                )
                // then
                .andExpect(status().isOk()).andDo(print())
        ;
    }

    @Test
    @DisplayName("사용자별 이력서 조회")
    void readResumeByUser() throws Exception {
        // given
        resumeRepository.save(buildResume());
        resumeRepository.save(buildResume());
        resumeRepository.save(buildResume());

        // when
        mockMvc.perform(get("/api/resume/user")
                        .with(user(mockedUser))
                        .header("Authorization", "Bearer " + accessToken)
                )

                // then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.*", hasSize(3)))
                .andDo(print())
        ;
    }

    @Test
    @DisplayName("이력서 수정")
    void updateResume() throws Exception {
        // given
        Resume resume = buildResume();
        resumeRepository.save(resume);
        UpdateResumeRequestDto updateResumeRequestDto = new UpdateResumeRequestDto("수정된 이력서 내용");

        // when
        mockMvc.perform(patch("/api/resume/{resumeId}", resume.getId())
                .with(user(mockedUser))
                .content(gson.toJson(new UpdateCommentRequestDto("바뀐 내용")))
                .header("Authorization", "Bearer " + accessToken)
        )
        // then

        ;
    }

    @Test
    @DisplayName("이력서 삭제")
    void testResume() throws Exception{
        //given
        Resume resume = buildResume();
        resumeRepository.save(resume);

        //when
        mockMvc.perform(delete("/api/resume/" + resume.getId())
                        .header("Authorization", "Bearer " + accessToken)
                )
                //then
                .andExpect(status().isOk()).andDo(print());
    }

}
