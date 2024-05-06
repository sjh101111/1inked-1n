package com.example.oneinkedoneproject.service.follow;

import com.example.oneinkedoneproject.domain.Follow;
import com.example.oneinkedoneproject.domain.User;
import com.example.oneinkedoneproject.dto.follow.AddFollowRequestDto;
import com.example.oneinkedoneproject.dto.follow.FollowResponseDto;
import com.example.oneinkedoneproject.repository.follow.FollowRepository;
import com.example.oneinkedoneproject.repository.user.UserRepository;
import com.example.oneinkedoneproject.utils.GenerateIdUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FollowService {
    private final FollowRepository followRepository;
    private final UserRepository userRepository;

//    public Follow save(User from_user,User to_user){
//        Follow follow = Follow.builder()
//                .toUser(to_user)
//                .fromUser(from_user)
//                .build();
//        return followRepository.save(follow);
//    }

//    @Transactional
//    public void follow(User follower, User followed) {
//        Optional<Follow> existingFollow = followRepository.findByFollowerAndFollowed(follower, followed);
//        if (!existingFollow.isPresent()) {
//            Follow follow = new Follow();
//            follow.setFollower(follower);
//            follow.setFollowed(followed);
//            followRepository.save(follow);
//        }
//    }

    @Transactional
    public Follow follow(AddFollowRequestDto request){

        User followUser = userRepository.findById(request.getFollowUserId())
                .orElseThrow(IllegalArgumentException::new);

        User followedUser = userRepository.findById(request.getFollowedUserId())
                .orElseThrow(IllegalArgumentException::new);

        return followRepository.save(new Follow(GenerateIdUtils.generateFollowId(), followUser, followedUser));
    }

    // 팔로우 생성 기능

//    @Transactional
//    public List<Follow> getFollowing(User user) {
//        return followRepository.findByToUser(user);
//    }
//
//    @Transactional
//    public List<Follow> getFollowers(User user) {
//        return followRepository.findByFromUser(user);
//    }


    @Transactional(readOnly = true)
    public List<FollowResponseDto> getFollows(){
        List<Follow> follows = followRepository.findAll();
        return follows.stream()
                .map(follow -> new FollowResponseDto(follow.getToUser().getRealname(), follow.getToUser().getIdentity(), follow.getToUser().getImage()))
                .collect(Collectors.toList());

    }

    // 팔로우 목록 조회 기능

    @Transactional(readOnly = true)
    public List<FollowResponseDto> getFollowers(){
        List<Follow> follows = followRepository.findAll();
        return follows.stream()
                .map(follow -> new FollowResponseDto(follow.getFromUser().getRealname(), follow.getFromUser().getIdentity(), follow.getFromUser().getImage()))
                .collect(Collectors.toList());

    }

    // 팔로워 목록 조회 기능

//    @Transactional
//    public boolean isFollowing(User follower, User followed) {
//        return followRepository.findByFollowerAndFollowed(follower, followed).isPresent();
//    }

//    @Transactional
//    public void unfollow(User follower, User followed) {
//        Optional<Follow> existingFollow = followRepository.findByFollowerAndFollowed(follower, followed);
//        existingFollow.ifPresent(followRepository::delete);
//    }

    @Transactional
    public void unfollow(String followId) {
//        Follow follow = followRepository.findById(followId)
//                .orElseThrow(IllegalArgumentException::new);
        followRepository.deleteById(followId);
    }

    // 팔로우 취소 기능


}
