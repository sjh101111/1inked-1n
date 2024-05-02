package com.example.oneinkedoneproject.repository.user;

import com.example.oneinkedoneproject.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,String> {
}
