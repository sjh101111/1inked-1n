package com.example.oneinkedoneproject.repository.resume;

import static org.assertj.core.api.Assertions.*;

import com.example.oneinkedoneproject.OneinkedOneProjectApplication;
import com.example.oneinkedoneproject.domain.Grade;
import com.example.oneinkedoneproject.domain.PasswordQuestion;
import com.example.oneinkedoneproject.domain.Resume;
import com.example.oneinkedoneproject.domain.User;
import com.example.oneinkedoneproject.repository.password.PasswordRepository;
import com.example.oneinkedoneproject.repository.user.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ContextConfiguration;

@DataJpaTest
public class ResumeRepositoryTest {

    @Autowired
    private ResumeRepository resumeRepository;

	@Autowired
	private PasswordRepository passwordRepository;

    @Autowired
    private UserRepository userRepository;

	private PasswordQuestion pwdQuestion;

	private User user;

	@BeforeEach
	void init(){
		pwdQuestion = new PasswordQuestion("1", "질문1");
		user = new User("1","김","2","123",pwdQuestion,"아","학생","서울","hi",false, (byte) 10, Grade.ROLE_BASIC);
		passwordRepository.save(pwdQuestion);
		userRepository.save(user);
	}

    @Test
    @DisplayName("Resume 저장 확인")
    void saveResume() {
        // given
        Resume resume = new Resume("1", "hi", user);
        // when
        Resume savedResume = resumeRepository.save(resume);
        // then
        assertThat(resume.getId()).isEqualTo(savedResume.getId());
        assertThat(resume.getContents()).isEqualTo(savedResume.getContents());
        assertThat(resume.getUser().getId()).isEqualTo(savedResume.getUser().getId());
        assertThat(resumeRepository.count()).isEqualTo(1);
    }

    @Test
    @DisplayName("Resume 조회 확인")
    void findResume() {
        // given
        User savedUser = userRepository.save(user);
        Resume resume = new Resume("1", "hi", savedUser);
        Resume savedResume = resumeRepository.save(resume);

        // when
        Resume findResume = resumeRepository.findById(savedResume.getId())
                .orElseThrow(IllegalArgumentException::new);

        // then
        assertThat(findResume.getId()).isEqualTo(savedResume.getId());
        assertThat(findResume.getContents()).isEqualTo(savedResume.getContents());
        assertThat(resume.getUser().getId()).isEqualTo(savedResume.getUser().getId());
    }

    @Test
    @DisplayName("Resume 수정 확인")
    void updateResume(){
        // given
        User savedUser = userRepository.save(user);
        Resume resume = new Resume("1", "hi", savedUser);
        Resume savedResume = resumeRepository.save(resume);

        // when
        Resume updatedResume = resumeRepository.findById(savedResume.getId())
                .orElseThrow(IllegalArgumentException::new);
        updatedResume.updateContents("hi1");

        //then
        assertThat(updatedResume).isNotNull();
        assertThat(updatedResume.getContents()).isEqualTo("hi1");
    }

    @Test
    @DisplayName("Resume 삭제 확인")
    void deleteResume(){
        // given
        User savedUser = userRepository.save(user);
        Resume resume = new Resume("1", "hi", savedUser);
        Resume savedResume = resumeRepository.save(resume);

        // when
        resumeRepository.delete(savedResume);

        //then
        assertThat(resumeRepository.existsById(savedResume.getId())).isFalse();
    }
}
