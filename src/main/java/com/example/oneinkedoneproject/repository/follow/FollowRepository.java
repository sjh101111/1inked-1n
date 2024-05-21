package com.example.oneinkedoneproject.repository.follow;

import com.example.oneinkedoneproject.domain.Follow;
import com.example.oneinkedoneproject.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


import java.util.List;
import java.util.Optional;

public interface FollowRepository extends JpaRepository<Follow, String> {
    List<Follow> findAllByFromUser_Email(String curUserEmail);

    List<Follow> findAllByToUser_Email(String curUserEmail);

    @Query("Select a From Follow a WHERE a.fromUser.id = :fromUserId and a.toUser.id = :userId")
    Optional<Follow> findByToUserId(String userId, String fromUserId);

}

