package com.example.oneinkedoneproject.repository;

import com.example.oneinkedoneproject.OneinkedOneProjectApplication;
import com.example.oneinkedoneproject.domain.Grade;
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
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;


    @Test
    @DisplayName("user 저장 확인")
    void saveMember(){
        // given
        User user = new User("1","김","2","123","음","아","학생","서울","hi",false, (byte) 10, Grade.ROLE_BASIC);

        //when
        User savedUser = userRepository.save(user);

        // then
        Assertions.assertThat(user.getId()).isEqualTo(savedUser.getId());
        Assertions.assertThat(user.getUsername()).isEqualTo(savedUser.getUsername());
        Assertions.assertThat(user.getEmail()).isEqualTo(savedUser.getEmail());
        Assertions.assertThat(user.getPassword()).isEqualTo(savedUser.getPassword());
        Assertions.assertThat(user.getPasswordQuestion()).isEqualTo(savedUser.getPasswordQuestion());
        Assertions.assertThat(user.getPasswordAnswer()).isEqualTo(savedUser.getPasswordAnswer());
        Assertions.assertThat(user.getIdentity()).isEqualTo(savedUser.getIdentity());
        Assertions.assertThat(user.getLocation()).isEqualTo(savedUser.getLocation());
        Assertions.assertThat(user.getDescription()).isEqualTo(savedUser.getDescription());
        Assertions.assertThat(user.getWithdraw()).isEqualTo(savedUser.getWithdraw());
        Assertions.assertThat(user.getImage()).isEqualTo(savedUser.getImage());
        Assertions.assertThat(user.getGrade()).isEqualTo(savedUser.getGrade());
        Assertions.assertThat(userRepository.count()).isEqualTo(1);
    }

    @Test
    @DisplayName("user 조회 확인")
    void findUser(){

        // given
        User user = new User("1","김","2","123","음","아","학생","서울","hi",false, (byte) 10, Grade.ROLE_BASIC);
        User savedUser = userRepository.save(user);

        // when
        User findUser = userRepository.findById(savedUser.getId())
                .orElseThrow(IllegalArgumentException::new);

        // then
        Assertions.assertThat(userRepository.count()).isEqualTo(1);
        Assertions.assertThat(findUser.getId()).isEqualTo(savedUser.getId());
        Assertions.assertThat(findUser.getUsername()).isEqualTo(savedUser.getUsername());
        Assertions.assertThat(findUser.getEmail()).isEqualTo(savedUser.getEmail());
        Assertions.assertThat(findUser.getPassword()).isEqualTo(savedUser.getPassword());
        Assertions.assertThat(findUser.getPasswordQuestion()).isEqualTo(savedUser.getPasswordQuestion());
        Assertions.assertThat(findUser.getPasswordAnswer()).isEqualTo(savedUser.getPasswordAnswer());
        Assertions.assertThat(findUser.getIdentity()).isEqualTo(savedUser.getIdentity());
        Assertions.assertThat(findUser.getLocation()).isEqualTo(savedUser.getLocation());
        Assertions.assertThat(findUser.getDescription()).isEqualTo(savedUser.getDescription());
        Assertions.assertThat(findUser.getWithdraw()).isEqualTo(savedUser.getWithdraw());
        Assertions.assertThat(findUser.getImage()).isEqualTo(savedUser.getImage());
        Assertions.assertThat(findUser.getGrade()).isEqualTo(savedUser.getGrade());

    }

    @Test
    @DisplayName("user 수정 확인")
    @Transactional
    void updateUser(){

        // given
        User user = new User("1","김","2","123","음","아","학생","서울","hi",false, (byte) 10, Grade.ROLE_BASIC);
        User savedUser = userRepository.save(user);

        // when
        User updatedUser = userRepository.findById(savedUser.getId())
                .orElseThrow(IllegalArgumentException::new);

        updatedUser.updateName("김민");

        //then
        Assertions.assertThat(updatedUser).isNotNull();
        Assertions.assertThat(updatedUser.getUsername()).isEqualTo("김민");

    }

    @Test
    @DisplayName("user 삭제 확인")
    void deleteUser(){

        // given
        User user = new User("1","김","2","123","음","아","학생","서울","hi",false, (byte) 10, Grade.ROLE_BASIC);
        User savedUser = userRepository.save(user);

        // when
        userRepository.delete(savedUser);

        //then
        Assertions.assertThat(userRepository.count()).isEqualTo(0);

    }



}
