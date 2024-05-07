package com.example.oneinkedoneproject.controller;

import com.example.oneinkedoneproject.domain.Follow;
import com.example.oneinkedoneproject.domain.User;
import com.example.oneinkedoneproject.dto.follow.AddFollowRequestDto;
import com.example.oneinkedoneproject.dto.follow.FollowResponseDto;
import com.example.oneinkedoneproject.service.follow.FollowService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class FollowController {


    private final FollowService followService;

    @PostMapping("/api/follow")
    public ResponseEntity<Follow> followUser(@RequestBody AddFollowRequestDto request, @AuthenticationPrincipal User fromUser) {
        return ResponseEntity.status(HttpStatus.CREATED).body(followService.follow(fromUser,request));
    }

    // follow 생성 api

//    @PostMapping("/api/follow")
//    public ResponseEntity<String> followUser(@RequestParam User followerId, @RequestParam User followedId) {
//        followService.follow(followerId, followedId);
//        return new ResponseEntity<>("Followed successfully", HttpStatus.CREATED);
//    }

//    @GetMapping("api/follows")
//    public ResponseEntity<List<FollowResponseDto>> getFollows(@RequestParam User userId) {
//        return followService.getFollowers(userId);
//    }

//    @GetMapping("/followers")
//    public List<Follow> getFollowers(@RequestParam User userId) {
//        return followService.getFollowers(userId);
//    }
//
//    @GetMapping("/following")
//    public List<Follow> getFollowing(@RequestParam User userId) {
//        return followService.getFollowing(userId);
//    }

//    @DeleteMapping("api/follow")
//    public ResponseEntity<String> unfollowUser(@RequestParam User followerId, @RequestParam User followedId) {
//        followService.unfollow(followerId, followedId);
//        return new ResponseEntity<>("Unfollowed successfully", HttpStatus.OK);
//    }

}