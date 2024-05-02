package com.example.oneinkedoneproject.repository.follow;

import com.example.oneinkedoneproject.OneinkedOneProjectApplication;
import com.example.oneinkedoneproject.domain.Follow;
import com.example.oneinkedoneproject.domain.Grade;
import com.example.oneinkedoneproject.domain.User;
import com.example.oneinkedoneproject.repository.follow.FollowRepository;
import com.example.oneinkedoneproject.repository.user.UserRepository;
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
public class FollowRepositoryTest {

    @Autowired
    private FollowRepository followRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("follow 저장 확인")
    void saveFollow(){

        // given
        User toUser = new User("1","김","2","123","음","아","학생","서울","hi",false, (byte) 10, Grade.ROLE_BASIC);
        userRepository.save(toUser);
        User fromUser = new User("2","김민","3","1234","음1","아1","학생1","부산","hi2",false, (byte) 10, Grade.ROLE_BASIC);
        userRepository.save(fromUser);
        Follow follow = new Follow("1", toUser, fromUser);

        //when
        Follow savedFollow = followRepository.save(follow);

        // then
        Assertions.assertThat(follow.getId()).isEqualTo(savedFollow.getId());
        Assertions.assertThat(follow.getToUser().getId()).isEqualTo(savedFollow.getToUser().getId());
        Assertions.assertThat(follow.getFromUser().getId()).isEqualTo(savedFollow.getFromUser().getId());
        Assertions.assertThat(followRepository.count()).isEqualTo(1);


    }

    @Test
    @DisplayName("Follow 조회 확인")
    void findFollow() {
        // given
        User user1 = new User("1","김","2","123","음","아","학생","서울","hi",false, (byte) 10, Grade.ROLE_BASIC);
        User savedUser = userRepository.save(user1);
        User user2 = new User("2","김민","3","1234","음1","아1","학생1","부산","hi2",false, (byte) 10, Grade.ROLE_BASIC);
        User savedUser2 = userRepository.save(user2);
        Follow follow = new Follow("1", user1, user2);
        Follow savedFollow = followRepository.save(follow);

        // when
        Follow findFollow = followRepository.findById(savedFollow.getId())
                .orElseThrow(IllegalArgumentException::new);


        // then
        Assertions.assertThat(findFollow.getId()).isEqualTo(savedFollow.getId());
        Assertions.assertThat(findFollow.getToUser().getId()).isEqualTo(savedFollow.getToUser().getId());
        Assertions.assertThat(findFollow.getFromUser().getId()).isEqualTo(savedFollow.getFromUser().getId());
    }

    @Test
    @DisplayName("Follow 수정 확인")
    void updateFollow() {
        // given
        User user1 = new User("1","김","2","123","음","아","학생","서울","hi",false, (byte) 10, Grade.ROLE_BASIC);
        User savedUser = userRepository.save(user1);
        User user2 = new User("2","김민","3","1234","음1","아1","학생1","부산","hi2",false, (byte) 10, Grade.ROLE_BASIC);
        User savedUser2 = userRepository.save(user2);
        User user3 = new User("3","김민구","4","12345","음12","아12","학생12","경기도","hi23",false, (byte) 10, Grade.ROLE_BASIC);
        User savedUser3 = userRepository.save(user3);
        Follow follow = new Follow("1", user1, user2);
        Follow savedFollow = followRepository.save(follow);

        // when
        Follow updatedFollow = followRepository.findById(savedFollow.getId())
                .orElseThrow(IllegalArgumentException::new);
        updatedFollow.changeFollow(savedUser3);

        // then
        Assertions.assertThat(updatedFollow).isNotNull();
        Assertions.assertThat(updatedFollow.getToUser().getId()).isEqualTo(savedUser3.getId());

    }

    @Test
    @DisplayName("Follow 삭제 확인")
    void deleteFollow(){

        // given
        User toUser = new User("1","김","2","123","음","아","학생","서울","hi",false, (byte) 10, Grade.ROLE_BASIC);
        userRepository.save(toUser);
        User fromUser = new User("2","김민","3","1234","음1","아1","학생1","부산","hi2",false, (byte) 10, Grade.ROLE_BASIC);
        userRepository.save(fromUser);
        Follow follow = new Follow("1", toUser, fromUser);
        Follow savedFollow = followRepository.save(follow);

        // when
        followRepository.delete(savedFollow);

        //then
        Assertions.assertThat(followRepository.existsById(savedFollow.getId())).isFalse();

    }




}
