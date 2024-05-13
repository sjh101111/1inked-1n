package com.example.oneinkedoneproject.repository;

import com.example.oneinkedoneproject.domain.Follow;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FollowRepository extends JpaRepository<Follow,String> {
}
