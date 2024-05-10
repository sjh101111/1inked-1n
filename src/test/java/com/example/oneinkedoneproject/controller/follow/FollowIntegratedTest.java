package com.example.oneinkedoneproject.controller.follow;

import com.example.oneinkedoneproject.domain.Follow;
import com.example.oneinkedoneproject.domain.Grade;
import com.example.oneinkedoneproject.domain.PasswordQuestion;
import com.example.oneinkedoneproject.domain.User;
import com.example.oneinkedoneproject.dto.auth.TokenInfo;
import com.example.oneinkedoneproject.dto.follow.AddFollowRequestDto;
import com.example.oneinkedoneproject.repository.follow.FollowRepository;
import com.example.oneinkedoneproject.repository.password.PasswordRepository;
import com.example.oneinkedoneproject.repository.user.UserRepository;
import com.example.oneinkedoneproject.service.auth.JwtTokenProvider;
import com.example.oneinkedoneproject.service.follow.FollowService;
import com.example.oneinkedoneproject.utils.GenerateIdUtils;
import com.google.gson.Gson;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Optional;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class FollowIntegratedTest {

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordRepository passwordRepository;

    @Autowired
    private FollowService followService;

    @Autowired
    private FollowRepository followRepository;

    private User followUser;
    private User followedUser;
    private User curUser;
    private Gson gson;
    private String accessToken;

    private final String url = "/api/follow";
    private final String getUrl1 = "/api/follows";
    private final String getUrl2 = "/api/followers";
    private final String deleteUrl = "/api/follow/{followId}";

    private Follow buildFollow(User followUser, User followedUser) {
        return Follow.builder()
                .id(GenerateIdUtils.generateFollowId())
                .toUser(followUser)
                .fromUser(followedUser)
                .build();
    }

    // setup
    @BeforeEach
    void setUp() {
        followRepository.deleteAll();
        userRepository.deleteAll();
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).
                apply(SecurityMockMvcConfigurers.springSecurity()).build();
        gson = new Gson();

        followUser = userRepository.save(User.builder()
                .withdraw(false)
                .email("test1@test.com")
                .password("1234")
                .id("1")
                .realname("lee")
                .grade(Grade.ROLE_BASIC)
                .passwordQuestion(
                        passwordRepository.save(PasswordQuestion.builder()
                                .id("1")
                                .question("?")
                                .build()))
                .build());

        followedUser = userRepository.save(User.builder()
                .withdraw(false)
                .email("test2@test.com")
                .password("1234")
                .id("2")
                .realname("park")
                .grade(Grade.ROLE_BASIC)
                .passwordQuestion(
                        passwordRepository.save(PasswordQuestion.builder()
                                .id("2")
                                .question("?")
                                .build()))
                .build());

        curUser = userRepository.save(User.builder()
                .withdraw(false)
                .email("test@test.com")
                .password("1234")
                .id("3")
                .realname("kim")
                .grade(Grade.ROLE_BASIC)
                .passwordQuestion(
                        passwordRepository.save(PasswordQuestion.builder()
                                .id("3")
                                .question("?")
                                .build()))
                .build());

        Authentication auth = new UsernamePasswordAuthenticationToken(curUser, null, curUser.getAuthorities());

        TokenInfo tokenInfo = jwtTokenProvider.createToken(auth);
//        String accessToken = tokenInfo.getAccessToken();

        // Set the JWT token in the security context (for internal use)
        SecurityContextHolder.getContext().setAuthentication(auth);

        // Store the accessToken for use in requests
        this.accessToken = tokenInfo.getAccessToken();
//        String accessToken = tokenInfo.getAccessToken();
    }

    @Test
    @DisplayName("팔로우 생성")
    void testCreateFollow() throws Exception {

        //given
        AddFollowRequestDto request = new AddFollowRequestDto("1");

        ResultActions resultActions = mockMvc.perform(post(url)
                        .header("Authorization", "Bearer " + accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(request))

                )
                // then
                .andExpect(status().isCreated()).andDo(print());
        //when

    }

    @Test
    @DisplayName("팔로우 조회")
    void showFollows() throws Exception {
        //given
        AddFollowRequestDto request = new AddFollowRequestDto("1");
        followService.follow(curUser, request);
        //when
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.get(getUrl1)
                .header("Authorization", "Bearer " + accessToken)
        );
        //then
        resultActions.andExpect(status().isOk()).andDo(print())
                .andExpect(jsonPath("$[0].realname").value("lee"));
    }

    @Test
    @DisplayName("팔로워 조회")
    void showFollowers() throws Exception {
        //given
        AddFollowRequestDto request = new AddFollowRequestDto("3");
        followService.follow(followedUser, request);
        //when
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.get(getUrl2)
                .header("Authorization", "Bearer " + accessToken)
        );
        //then
        resultActions.andExpect(status().isOk()).andDo(print())
                .andExpect(jsonPath("$[0].realname").value("park"));
    }

    @Test
    @DisplayName("팔로우 삭제")
    void testunfollow() throws Exception{
        //given
        AddFollowRequestDto request = new AddFollowRequestDto("1");
        Follow follow = followService.follow(curUser, request);

        //when
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/follow/" + follow.getId())
                        .header("Authorization", "Bearer " + accessToken)
                )
                //then
                .andExpect(status().isOk()).andDo(print());

        Optional<Follow> deleteVerify = followRepository.findById(follow.getId());

        Assertions.assertFalse(deleteVerify.isPresent());
    }
}













