package com.example.oneinkedoneproject.controller.follow;

import com.example.oneinkedoneproject.domain.Follow;
import com.example.oneinkedoneproject.domain.Grade;
import com.example.oneinkedoneproject.domain.User;
import com.example.oneinkedoneproject.dto.follow.AddFollowRequestDto;
import com.example.oneinkedoneproject.dto.follow.FollowResponseDto;
import com.example.oneinkedoneproject.service.follow.FollowService;
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
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class FollowControllerUnitTest {


    @Mock
    FollowService followService;
    @InjectMocks
    FollowController followController;

    private MockMvc mockMvc;
    private Gson gson;
    private User followUser;
    private User followedUser;
    private User curUser;
    private final String url = "/api/follow";
    private final String getUrl1 = "/api/follows";
    private final String getUrl2 = "/api/followers";
    private final String deleteUrl = "/api/follow/{followId}";



    @BeforeEach
    public void init(){

        mockMvc = MockMvcBuilders.standaloneSetup(followController).build();
        gson = new Gson();

        followUser =
                User.builder()
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
                .grade(Grade.ROLE_BASIC)
                .build();


        followedUser =
                User.builder()
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
                .grade(Grade.ROLE_BASIC)
                .build();


        curUser =
                User.builder()
                .id(GenerateIdUtils.generateUserId())
                .realname("park")
                .email("test")
                .password("1234")
                .passwordQuestion(null)
                .passwordAnswer("답변")
                .identity("학생")
                .location("인천")
                .description("hi")
                .withdraw(false)
                .grade(Grade.ROLE_BASIC)
                .build();

    }
        private Follow buildFollow(User followUser,User followedUser){
            return Follow.builder()
                    .id(GenerateIdUtils.generateFollowId())
                    .toUser(followUser)
                    .fromUser(followedUser)
                    .build();

    }

    @Test
    @DisplayName("follow 생성")
    void follow() throws Exception {

        // given
        AddFollowRequestDto request = new AddFollowRequestDto("testUser");
        Follow follow = buildFollow(followUser, followedUser);
        doReturn(follow).when(followService).follow(any(User.class) , any(AddFollowRequestDto.class));

        // when
        ResultActions resultActions = mockMvc.perform(
                post(url).with(user(followedUser))
                        .content(gson.toJson(request))
                        .contentType(MediaType.APPLICATION_JSON)
        );

        // then
        resultActions.andExpect(status().isCreated())
                .andDo(print());

    }

    @Test
    @DisplayName("follows 조회")
    void getFollows() throws Exception {

        List<Follow> followList = new ArrayList<>();
        Follow testFollow = buildFollow(followUser, curUser);
        followList.add(testFollow);

        List<FollowResponseDto> follows = followList.stream()
                .map(follow -> new FollowResponseDto(testFollow.getId(), follow.getToUser().getRealname(), follow.getToUser().getIdentity(), follow.getToUser().getImage(), follow.getToUser().getEmail(), follow.getToUser().getId()))
                .collect(Collectors.toList());

        doReturn(follows).when(followService).getFollows(any(User.class));

        // when
        ResultActions resultActions = mockMvc.perform(get(getUrl1));

        //then
        resultActions.andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("followers 조회")
    void getFollowers() throws Exception {

        List<Follow> followList = new ArrayList<>();
        Follow testFollow = buildFollow(curUser, followedUser);
        followList.add(testFollow);

        List<FollowResponseDto> follows = followList.stream()
                .map(follow -> new FollowResponseDto(follow.getId(), follow.getFromUser().getRealname(), follow.getFromUser().getIdentity(), follow.getFromUser().getImage(), follow.getFromUser().getEmail(), follow.getFromUser().getId()))
                .collect(Collectors.toList());

        doReturn(follows).when(followService).getFollowers(any(User.class));

        // when
        ResultActions resultActions = mockMvc.perform(get(getUrl2));

        //then
        resultActions.andExpect(status().isOk())
                .andDo(print());

    }

    @Test
    @DisplayName("follow 삭제")
    void deleteFollow() throws Exception{

        //given
        Follow follow = buildFollow(followUser, followedUser);

        //when
        ResultActions resultActions = mockMvc.perform(delete(deleteUrl, followUser.getId()))
                .andExpect(status().isOk());

        // then
        verify(followService, only()).unfollow(any(String.class), any(User.class));
    }









}
