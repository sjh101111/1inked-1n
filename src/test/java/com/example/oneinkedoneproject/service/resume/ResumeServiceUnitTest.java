package com.example.oneinkedoneproject.service.resume;

import com.example.oneinkedoneproject.domain.Grade;
import com.example.oneinkedoneproject.domain.PasswordQuestion;
import com.example.oneinkedoneproject.domain.Resume;
import com.example.oneinkedoneproject.domain.User;
import com.example.oneinkedoneproject.dto.resume.AddResumeRequestDto;
import com.example.oneinkedoneproject.dto.resume.ResumeResponseDto;
import com.example.oneinkedoneproject.dto.resume.UpdateResumeRequestDto;
import com.example.oneinkedoneproject.repository.resume.ResumeRepository;
import com.example.oneinkedoneproject.repository.user.UserRepository;
import com.example.oneinkedoneproject.utils.GenerateIdUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ResumeServiceUnitTest {
    @Mock
    private ResumeRepository resumeRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private ResumeService resumeService;

    private Resume resume;
    private User user;

    @BeforeEach
    void init() {
        PasswordQuestion pq = PasswordQuestion.builder()
                .id("1")
                .question("?")
                .build();

        user = User.builder()
                .withdraw(false)
                .email("test@test.com")
                .password("1234")
                .id("1")
                .realname("name")
                .grade(Grade.ROLE_BASIC)
                .passwordQuestion(pq)
                .build();

        resume = Resume.builder()
                .id(GenerateIdUtils.generateResumeId())
                .contents("Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum.")
                .user(user)
                .build();
    }

    @Test
    @DisplayName("이력서 저장")
    void saveResume() {
        // given
        AddResumeRequestDto addResumeRequestDto = new AddResumeRequestDto(resume.getContents());
        doReturn(resume).when(resumeRepository).save(any(Resume.class));

        // when
        Resume savedResume = resumeService.save(addResumeRequestDto, user);

        // then
        assertThat(savedResume.getContents()).isEqualTo(resume.getContents());
    }

    @Test
    @DisplayName("이력서 조회")
    void getResume() {
        // given
        doReturn(Optional.of(resume)).when(resumeRepository).findById(resume.getId());

        // when
        Resume findResume = resumeService.findById(resume.getId());

        // then
        assertThat(findResume).isEqualTo(resume);
    }

    @Test
    @DisplayName("사용자 기반 이력서 목록 조회")
    void getResumeList() {
        // given
        Resume resume1 = Resume.builder()
                .id(GenerateIdUtils.generateResumeId())
                .contents("Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum.")
                .user(user)
                .build();

        List<Resume> resumeList = new ArrayList<>();
        resumeList.add(resume);
        resumeList.add(resume1);

        doReturn(resumeList).when(resumeRepository).findByUser(user);

        // when
        List<Resume> findResumeList = resumeService.findByUser(user);

        // then
        assertThat(resumeList.size()).isEqualTo(2);
        assertThat(resumeList.get(0).getId()).isEqualTo(resume.getId());
    }

    @Test
    @DisplayName("특정 사용자 Resume 조회")
    void getResumeOfUserPage() {
        List<Resume> resumes = new ArrayList<>();
        resumes.add(resume);
        doReturn(Optional.of(user)).when(userRepository).findByEmail(any(String.class));
        doReturn(resumes).when(resumeRepository).findByUser(any(User.class));

        List<ResumeResponseDto> dto = resumeService.findByEmail(user.getEmail());

        assertThat(dto.get(0).getContents()).isEqualTo(resume.getContents());
    }

    @Test
    @DisplayName("이력서 수정")
    void updateResume() {
        // given
        UpdateResumeRequestDto updateResumeRequestDto = new UpdateResumeRequestDto("updated resume contents");
        doReturn(Optional.of(resume)).when(resumeRepository).findById(resume.getId());

        // when
        Resume updatedResume = resumeService.update(resume.getId(), updateResumeRequestDto);

        // then
        assertThat(updatedResume.getContents()).isEqualTo(updateResumeRequestDto.getContents());
    }

    @Test
    @DisplayName("이력서 삭제")
    void deleteResume() {
        // given
        doNothing().when(resumeRepository).deleteById(resume.getId());

        // when
        resumeService.delete(resume.getId());

        // then
        verify(resumeRepository, times(1)).deleteById(resume.getId());
    }
}
