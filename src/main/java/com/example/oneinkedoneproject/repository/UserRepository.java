package com.example.oneinkedoneproject.repository;

import com.example.oneinkedoneproject.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,String> {
}
