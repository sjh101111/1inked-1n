package com.example.oneinkedoneproject.controller.follow;

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

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class FollowController {

    private final FollowService followService;

    @PostMapping("/api/follow")
    public ResponseEntity<Follow> followUser(@RequestBody AddFollowRequestDto request, @AuthenticationPrincipal User fromUser) {
        return ResponseEntity.status(HttpStatus.CREATED).body(followService.follow(fromUser, request));
    }

    @GetMapping("/api/follows")
    public ResponseEntity<List<FollowResponseDto>> getFollows(@AuthenticationPrincipal User toUser) {
        return ResponseEntity.ok(followService.getFollows(toUser));
    }

    @GetMapping("/api/followsOfUser/{email}")
    public ResponseEntity<List<FollowResponseDto>> getFollowsOfUser(@PathVariable String email) throws Exception {
        String decodedEmail = URLDecoder.decode(email, StandardCharsets.UTF_8.toString());
        return ResponseEntity.ok(followService.getFollowsOfUser(decodedEmail));
    }

    // follows
    @GetMapping("/api/followers")
    public ResponseEntity<List<FollowResponseDto>> getFollowers(@AuthenticationPrincipal User fromUser) {
        return ResponseEntity.ok(followService.getFollowers(fromUser));
    }


    @GetMapping("/api/followersOfUser/{email}")
    public ResponseEntity<List<FollowResponseDto>> getFollowersOfUser(@PathVariable String email) throws Exception {
        String decodedEmail = URLDecoder.decode(email, StandardCharsets.UTF_8.toString());
        return ResponseEntity.ok(followService.getFollowersOfUser(decodedEmail));
    }

    @DeleteMapping("/api/follow/{userId}")
    public ResponseEntity<Void> unfollow(@PathVariable String userId, @AuthenticationPrincipal User fromUser) {
        followService.unfollow(userId, fromUser);
        return ResponseEntity.ok().body(null);
    }

}