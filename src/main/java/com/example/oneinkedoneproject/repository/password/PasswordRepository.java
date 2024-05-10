package com.example.oneinkedoneproject.repository.password;

import com.example.oneinkedoneproject.domain.PasswordQuestion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PasswordRepository extends JpaRepository<PasswordQuestion, String> {
}
