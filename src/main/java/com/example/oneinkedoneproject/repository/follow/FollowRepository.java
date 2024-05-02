package com.example.oneinkedoneproject.repository.follow;

import com.example.oneinkedoneproject.domain.Follow;
import com.example.oneinkedoneproject.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FollowRepository extends JpaRepository<Follow,String> {
    List<Follow> findByFromUser(User follower);

    List<Follow> findByToUser(User followed);

    Optional<Follow> findByFollowerAndFollowed(User follower, User followed);

    void deleteFollowByFromUser(User user);
}

