package com.example.oneinkedoneproject.service.follow;

import com.example.oneinkedoneproject.domain.Follow;
import com.example.oneinkedoneproject.domain.Grade;
import com.example.oneinkedoneproject.domain.User;
import com.example.oneinkedoneproject.dto.follow.AddFollowRequestDto;
import com.example.oneinkedoneproject.dto.follow.FollowResponseDto;
import com.example.oneinkedoneproject.repository.follow.FollowRepository;
import com.example.oneinkedoneproject.repository.user.UserRepository;
import com.example.oneinkedoneproject.utils.GenerateIdUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FollowServiceUnitTest {

    @Mock
    private FollowRepository followRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private FollowService followService;


    @Test
    @DisplayName("follow 생성")
    void follow(){

        // given

        AddFollowRequestDto request = new AddFollowRequestDto("1", "2");

        when(userRepository.findById(any(String.class))).thenReturn(
                Optional.of(User.builder()
                        .id(GenerateIdUtils.generateUserId())
                        .realname("kim")
                        .email("test")
                        .password("1234")
                        .passwordQuestion(null)
                        .passwordAnswer("답변")
                        .identity("학생")
                        .location("서울")
                        .description("hi")
                        .withdraw(false)
                        .image((byte) 10)
                        .grade(Grade.ROLE_BASIC)
                        .build())
        );


//        when(userRepository.findById(any(String.class))).thenReturn(
//                Optional.of(User.builder()
//                        .id(GenerateIdUtils.generateUserId())
//                        .realname("lee")
//                        .email("test2")
//                        .password("12345")
//                        .passwordQuestion(null)
//                        .passwordAnswer("답변2")
//                        .identity("직장인")
//                        .location("부산")
//                        .description("hi2")
//                        .withdraw(false)
//                        .image((byte) 10)
//                        .grade(Grade.ROLE_BASIC)
//                        .build())
//        );

        Follow follow = Follow.builder()
                .id(GenerateIdUtils.generateFollowId())
                .toUser(null)
                .fromUser(null)
                .build();

        when(followRepository.save(any(Follow.class))).thenReturn(follow);
//        doReturn(follow).when(followRepository).save(any(Follow.class));

        // when
        Follow savedFollow = followService.follow(request);

        // then
        assertThat(savedFollow).isNotNull();
        assertThat(savedFollow.getId()).isEqualTo(follow.getId());


    }

    @Test
    @DisplayName("follow 실패 - 존재하지 않는 user를 follow 할 경우")
    void followFail(){
        // given
        // 없는 Follow , Follower user를 찾는다고 가정
        AddFollowRequestDto request = new AddFollowRequestDto("notExistUser", "notExistUser");

        doReturn(Optional.empty()).when(userRepository).findById(any(String.class));

        //when
        assertThatThrownBy(() -> followService.follow(request)).isInstanceOf(IllegalArgumentException.class);


    }

    @Test
    @DisplayName("팔로잉 목록 조회")
    void showFollows(){

        List<Follow> followList = new ArrayList<>();

        //given
        User followUser = User.builder()
                .id(GenerateIdUtils.generateUserId())
                .realname("kim")
                .email("test")
                .password("1234")
                .passwordQuestion(null)
                .passwordAnswer("답변")
                .identity("학생")
                .location("서울")
                .description("hi")
                .withdraw(false)
                .image((byte) 10)
                .grade(Grade.ROLE_BASIC)
                .build();

        User followedUser = User.builder()
                .id(GenerateIdUtils.generateUserId())
                .realname("lee")
                .email("test2")
                .password("12345")
                .passwordQuestion(null)
                .passwordAnswer("답변2")
                .identity("직장인")
                .location("부산")
                .description("hi2")
                .withdraw(false)
                .image((byte) 10)
                .grade(Grade.ROLE_BASIC)
                .build();

        Follow follow = Follow.builder()
                .id(GenerateIdUtils.generateFollowId())
                .toUser(followUser)
                .fromUser(followedUser)
                .build();

        followList.add(follow);

        when(followRepository.findAll()).thenReturn(followList);


        // when
        List<FollowResponseDto> followsList = followService.getFollows();

        //then
        assertThat(followsList.size()).isEqualTo(1);
        assertThat(followsList.get(0).getRealname()).isEqualTo(followUser.getRealname());

    }

    @Test
    @DisplayName("팔로워 목록 조회")
    void showFollowers(){

        List<Follow> followList = new ArrayList<>();

        //given
        User followUser = User.builder()
                .id(GenerateIdUtils.generateUserId())
                .realname("kim")
                .email("test")
                .password("1234")
                .passwordQuestion(null)
                .passwordAnswer("답변")
                .identity("학생")
                .location("서울")
                .description("hi")
                .withdraw(false)
                .image((byte) 10)
                .grade(Grade.ROLE_BASIC)
                .build();

        User followedUser = User.builder()
                .id(GenerateIdUtils.generateUserId())
                .realname("lee")
                .email("test2")
                .password("12345")
                .passwordQuestion(null)
                .passwordAnswer("답변2")
                .identity("직장인")
                .location("부산")
                .description("hi2")
                .withdraw(false)
                .image((byte) 10)
                .grade(Grade.ROLE_BASIC)
                .build();

        Follow follow = Follow.builder()
                .id(GenerateIdUtils.generateFollowId())
                .toUser(followUser)
                .fromUser(followedUser)
                .build();

        followList.add(follow);

        when(followRepository.findAll()).thenReturn(followList);


        // when
        List<FollowResponseDto> followsList = followService.getFollowers();

        //then
        assertThat(followsList.size()).isEqualTo(1);
        assertThat(followsList.get(0).getRealname()).isEqualTo(followedUser.getRealname());

    }

    @Test
    @DisplayName("팔로우 삭제")
    void unfollow(){

        // given
        doNothing().when(followRepository).deleteById(any(String.class));

        // when
        followService.unfollow("1");

        // then
        verify(followRepository, only()).deleteById(any(String.class));


    }



}
