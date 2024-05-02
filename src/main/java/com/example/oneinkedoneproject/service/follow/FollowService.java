package com.example.oneinkedoneproject.service.follow;

import com.example.oneinkedoneproject.domain.Follow;
import com.example.oneinkedoneproject.domain.User;
import com.example.oneinkedoneproject.repository.follow.FollowRepository;
import com.example.oneinkedoneproject.repository.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class FollowService {

    @Autowired
    private FollowRepository followRepository;
    private UserRepository userRepository;

//    public Follow save(User from_user,User to_user){
//        Follow follow = Follow.builder()
//                .toUser(to_user)
//                .fromUser(from_user)
//                .build();
//        return followRepository.save(follow);
//    }

    public void follow(User follower, User followed) {
        Optional<Follow> existingFollow = followRepository.findByFollowerAndFollowed(follower, followed);
        if (!existingFollow.isPresent()) {
            Follow follow = new Follow();
            follow.setFollower(follower);
            follow.setFollowed(followed);
            followRepository.save(follow);
        }
    }

    public void unfollow(User follower, User followed) {
        Optional<Follow> existingFollow = followRepository.findByFollowerAndFollowed(follower, followed);
        existingFollow.ifPresent(followRepository::delete);
    }

    public boolean isFollowing(User follower, User followed) {
        return followRepository.findByFollowerAndFollowed(follower, followed).isPresent();
    }

    public List<Follow> getFollowers(User user) {
        return followRepository.findByFromUser(user);
    }

    public List<Follow> getFollowing(User user) {
        return followRepository.findByToUser(user);
    }
}
