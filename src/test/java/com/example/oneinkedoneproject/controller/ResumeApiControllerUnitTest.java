package com.example.oneinkedoneproject.controller;

import com.example.oneinkedoneproject.controller.resume.ResumeApiController;
import com.example.oneinkedoneproject.domain.*;
import com.example.oneinkedoneproject.dto.resume.AddResumeRequestDto;
import com.example.oneinkedoneproject.dto.resume.UpdateResumeRequestDto;
import com.example.oneinkedoneproject.service.resume.ResumeService;
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
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class ResumeApiControllerUnitTest {
    @Mock
    ResumeService resumeService;

    @InjectMocks
    ResumeApiController resumeApiController;

    private MockMvc mockMvc;
    private Gson gson;
    private static User mockedUser;

    @BeforeEach
    void init() {
        mockMvc = MockMvcBuilders.standaloneSetup(resumeApiController).build();
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
    }

    private Resume buildResume() {
        return Resume.builder()
                .id(GenerateIdUtils.generateResumeId())
                .contents("Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum.")
                .user(mockedUser)
                .build();
    }

    @Test
    @DisplayName("이력서 생성")
    void saveResume() throws Exception {
        // given
        Resume resume = buildResume();
        doReturn(resume).when(resumeService).save(any(AddResumeRequestDto.class), any(User.class));

        // when
        mockMvc.perform(post("/api/resume")
                        .with(user(mockedUser))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(new AddResumeRequestDto(" ")))
                )
                // then
                .andExpect(status().isCreated())
                .andDo(print())
        ;
    }

    @Test
    @DisplayName("이력서 조회")
    void getResume() throws Exception {
        // given
        Resume resume = buildResume();
        doReturn(resume).when(resumeService).findById(resume.getId());

        // when
        mockMvc.perform(get("/api/resume/{resumeId}", resume.getId())
                        .with(user(mockedUser))
                )

                // then
                .andExpect(status().isOk())
                .andDo(print())
        ;
    }

    @Test
    @DisplayName("사용자 기반 이력서 조회")
    void getResumeByUser() throws Exception {
        // given
        List<Resume> resumeList = new ArrayList<>();
        resumeList.add(buildResume());
        resumeList.add(buildResume());

        doReturn(resumeList).when(resumeService).findByUser(any(User.class));

        // when
        mockMvc.perform(get("/api/resume/user")
                        .with(user(mockedUser))
                )

                // then
                .andExpect(status().isOk())
                .andDo(print())
        ;
    }

    @Test
    @DisplayName("이력서 수정")
    void updateResume() throws Exception {
        // given
        Resume resume = buildResume();
        UpdateResumeRequestDto updateResumeRequestDto = new UpdateResumeRequestDto("이력서 내용");
        Resume updatedResume = new Resume(resume.getId(), updateResumeRequestDto.getContents(), resume.getUser());
        doReturn(updatedResume).when(resumeService).update(any(String.class), any(UpdateResumeRequestDto.class));

        // when
        mockMvc.perform(patch("/api/resume/{resumeId}", resume.getId())
                        .with(user(mockedUser))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(updateResumeRequestDto))
                )
                // then
                .andExpect(status().isOk())
                .andDo(print())
        ;

    }
}
