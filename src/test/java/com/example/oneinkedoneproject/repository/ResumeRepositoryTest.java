package com.example.oneinkedoneproject.repository;

import com.example.oneinkedoneproject.OneinkedOneProjectApplication;
import com.example.oneinkedoneproject.domain.Grade;
import com.example.oneinkedoneproject.domain.Resume;
import com.example.oneinkedoneproject.domain.User;
import com.example.oneinkedoneproject.repository.ResumeRepository;
import com.example.oneinkedoneproject.repository.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

@SpringBootTest
@ContextConfiguration(classes = OneinkedOneProjectApplication.class)
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
        Assertions.assertThat(savedResume.getId()).isNotNull();
        Assertions.assertThat(resume.getContents()).isEqualTo(savedResume.getContents());
        Assertions.assertThat(resume.getUser()).isEqualTo(savedResume.getUser());
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
        Assertions.assertThat(resumeRepository.count()).isEqualTo(1);
        Assertions.assertThat(findResume.getId()).isEqualTo("1");
        Assertions.assertThat(findResume.getContents()).isEqualTo("hi");
        Assertions.assertThat(findResume.getUser()).isEqualTo(savedUser);
    }

    @Test
    @DisplayName("resume 수정 확인")
    void updateResume(){

        // given
        User user = new User("1","김","2","123","음","아","학생","서울","hi",false, (byte) 10, Grade.ROLE_BASIC);
        User savedUser = userRepository.save(user);

        // when
        savedUser.updateName("김민");

        //then
        User findUser = userRepository.findById(savedUser.getId())
                .orElseThrow(IllegalArgumentException::new);
        Assertions.assertThat(findUser).isNotNull();
        Assertions.assertThat(findUser.getUsername()).isEqualTo("김민");
    }

    @Test
    @DisplayName("resume 삭제 확인")
    void deleteResume(){

        // given
        User user = new User("1","김","2","123","음","아","학생","서울","hi",false, (byte) 10, Grade.ROLE_BASIC);
        User savedUser = userRepository.save(user);

        // when
        userRepository.delete(savedUser);

        //then
        Assertions.assertThat(userRepository.count()).isEqualTo(0);

    }




}
