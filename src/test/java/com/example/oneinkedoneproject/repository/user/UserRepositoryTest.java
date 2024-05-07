package com.example.oneinkedoneproject.repository.user;

import static org.assertj.core.api.Assertions.*;

import com.example.oneinkedoneproject.domain.Grade;
import com.example.oneinkedoneproject.domain.PasswordQuestion;
import com.example.oneinkedoneproject.domain.User;
import com.example.oneinkedoneproject.repository.password.PasswordRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordRepository passwordRepository;

    private PasswordQuestion pwdQuestion;

    @BeforeEach
    void init(){
        pwdQuestion = new PasswordQuestion("1", "질문1");
        passwordRepository.save(pwdQuestion);
    }

    @Test
    @DisplayName("User 저장 확인")
    void saveMember(){
        // given
        User user = new User("1","김","2","123",pwdQuestion,"아","학생","서울","hi",false, null, Grade.ROLE_BASIC);

        //when
        User savedUser = userRepository.save(user);

        // then
        assertThat(user.getId()).isEqualTo(savedUser.getId());
        assertThat(user.getUsername()).isEqualTo(savedUser.getUsername());
        assertThat(user.getEmail()).isEqualTo(savedUser.getEmail());
        assertThat(user.getPassword()).isEqualTo(savedUser.getPassword());
        assertThat(user.getPasswordQuestion().getId()).isEqualTo(savedUser.getPasswordQuestion().getId());
        assertThat(user.getPasswordAnswer()).isEqualTo(savedUser.getPasswordAnswer());
        assertThat(user.getIdentity()).isEqualTo(savedUser.getIdentity());
        assertThat(user.getLocation()).isEqualTo(savedUser.getLocation());
        assertThat(user.getDescription()).isEqualTo(savedUser.getDescription());
        assertThat(user.getWithdraw()).isEqualTo(savedUser.getWithdraw());
        assertThat(user.getImage()).isEqualTo(savedUser.getImage());
        assertThat(user.getGrade()).isEqualTo(savedUser.getGrade());
        assertThat(userRepository.count()).isEqualTo(1);
    }

    @Test
    @DisplayName("User 조회 확인")
    void findUser(){
        // given
        User user = new User("1","김","2","123",pwdQuestion,"아","학생","서울","hi",false, null, Grade.ROLE_BASIC);
        User savedUser = userRepository.save(user);

        // when
        User findUser = userRepository.findById(savedUser.getId())
                .orElseThrow(IllegalArgumentException::new);

        // then
        assertThat(userRepository.count()).isEqualTo(1);
        assertThat(findUser.getId()).isEqualTo(savedUser.getId());
        assertThat(findUser.getUsername()).isEqualTo(savedUser.getUsername());
        assertThat(findUser.getEmail()).isEqualTo(savedUser.getEmail());
        assertThat(findUser.getPassword()).isEqualTo(savedUser.getPassword());
        assertThat(findUser.getPasswordQuestion()).isEqualTo(savedUser.getPasswordQuestion());
        assertThat(findUser.getPasswordAnswer()).isEqualTo(savedUser.getPasswordAnswer());
        assertThat(findUser.getIdentity()).isEqualTo(savedUser.getIdentity());
        assertThat(findUser.getLocation()).isEqualTo(savedUser.getLocation());
        assertThat(findUser.getDescription()).isEqualTo(savedUser.getDescription());
        assertThat(findUser.getWithdraw()).isEqualTo(savedUser.getWithdraw());
        assertThat(findUser.getImage()).isEqualTo(savedUser.getImage());
        assertThat(findUser.getGrade()).isEqualTo(savedUser.getGrade());
    }

    @Test
    @DisplayName("User 수정 확인")
    void updateUser(){
        // given
        User user = new User("1","김","2","123",pwdQuestion,"아","학생","서울","hi",false, null, Grade.ROLE_BASIC);
        User savedUser = userRepository.save(user);

        // when
        User updatedUser = userRepository.findById(savedUser.getId())
                .orElseThrow(IllegalArgumentException::new);
        updatedUser.updateName("김민");

        //then
        assertThat(updatedUser).isNotNull();
        assertThat(updatedUser.getUsername()).isEqualTo("김민");
    }

    @Test
    @DisplayName("User 삭제 확인")
    void deleteUser() {
        // given
        User user = new User("1", "김", "2", "123", pwdQuestion, "아", "학생", "서울", "hi", false, null, Grade.ROLE_BASIC);
        User savedUser = userRepository.save(user);

        // when
        userRepository.delete(savedUser);

        // then
        assertThat(userRepository.existsById(user.getId())).isFalse(); // 사용자가 삭제되었는지 확인
    }

    @Test
    @DisplayName("Email db존재 여부 확인")
    void existEmailTest(){
        //given
        User user = new User("1", "김", "2", "123", pwdQuestion, "아", "학생", "서울", "hi", false, null, Grade.ROLE_BASIC);
        boolean isExist = false;
        userRepository.save(user);

        //when
        isExist = userRepository.existsByEmail(user.getEmail());

        //then
        assertThat(isExist).isTrue();
    }

    @Test
    @DisplayName("email로 select 확인")
    void findByEmailTest(){
        //given
        User user = new User("1", "김", "2", "123", pwdQuestion, "아", "학생", "서울", "hi", false, null, Grade.ROLE_BASIC);
        userRepository.save(user);

        //when

        //then
        assertThat(userRepository.findByEmail(user.getEmail()).get().getId()).isEqualTo(user.getId());
    }
}
