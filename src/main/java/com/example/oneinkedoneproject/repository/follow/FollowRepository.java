package com.example.oneinkedoneproject.repository.follow;

import com.example.oneinkedoneproject.domain.Follow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FollowRepository extends JpaRepository<Follow,String> {
}
