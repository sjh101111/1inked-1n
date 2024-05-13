package com.example.oneinkedoneproject.repository;

import com.example.oneinkedoneproject.OneinkedOneProjectApplication;
import com.example.oneinkedoneproject.domain.Grade;
import com.example.oneinkedoneproject.domain.Resume;
import com.example.oneinkedoneproject.domain.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@ContextConfiguration(classes = OneinkedOneProjectApplication.class)
@Transactional
public class ResumeRepositoryTest {

    @Autowired
    private ResumeRepository resumeRepository;

    @Autowired
    private UserRepository userRepository;


    @Test
    @DisplayName("Resume 저장 확인")
    void saveResume() {
        // given
        User user = new User("1","김","2","123","음","아","학생","서울","hi",false, (byte) 10, Grade.ROLE_BASIC);
        Resume resume = new Resume("1", "hi", user);
        // when
        Resume savedResume = resumeRepository.save(resume);
        // then
        Assertions.assertThat(resume.getId()).isEqualTo(savedResume.getId());
        Assertions.assertThat(resume.getContents()).isEqualTo(savedResume.getContents());
        Assertions.assertThat(resume.getUser().getId()).isEqualTo(savedResume.getUser().getId());
        Assertions.assertThat(resumeRepository.count()).isEqualTo(1);
    }

    @Test
    @DisplayName("Resume 조회 확인")
    void findResume() {
        // given
        User user = new User("1","김","2","123","음","아","학생","서울","hi",false, (byte) 10, Grade.ROLE_BASIC);
        User savedUser = userRepository.save(user);
        Resume resume = new Resume("1", "hi", savedUser);
        Resume savedResume = resumeRepository.save(resume);

        // when
        Resume findResume = resumeRepository.findById(savedResume.getId())
                .orElseThrow(IllegalArgumentException::new);

        User findUser = userRepository.findById(savedUser.getId())
                .orElseThrow(IllegalArgumentException::new);


        // then
        Assertions.assertThat(findResume.getId()).isEqualTo(savedResume.getId());
        Assertions.assertThat(findResume.getContents()).isEqualTo(savedResume.getContents());
        Assertions.assertThat(resume.getUser().getId()).isEqualTo(savedResume.getUser().getId());
    }

    @Test
    @DisplayName("Resume 수정 확인")
    void updateResume(){

        // given
        User user = new User("1","김","2","123","음","아","학생","서울","hi",false, (byte) 10, Grade.ROLE_BASIC);
        User savedUser = userRepository.save(user);
        Resume resume = new Resume("1", "hi", savedUser);
        Resume savedResume = resumeRepository.save(resume);

        // when
        Resume updatedResume = resumeRepository.findById(savedResume.getId())
                .orElseThrow(IllegalArgumentException::new);
        updatedResume.updateContents("hi1");

        //then
        Assertions.assertThat(updatedResume).isNotNull();
        Assertions.assertThat(updatedResume.getContents()).isEqualTo("hi1");
    }

    @Test
    @DisplayName("Resume 삭제 확인")
    void deleteResume(){

        // given
        User user = new User("1","김","2","123","음","아","학생","서울","hi",false, (byte) 10, Grade.ROLE_BASIC);
        User savedUser = userRepository.save(user);
        Resume resume = new Resume("1", "hi", savedUser);
        Resume savedResume = resumeRepository.save(resume);

        // when
        resumeRepository.delete(savedResume);

        //then
        Assertions.assertThat(resumeRepository.existsById(savedResume.getId())).isFalse();

    }




}
