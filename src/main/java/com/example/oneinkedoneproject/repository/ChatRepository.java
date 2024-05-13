package com.example.oneinkedoneproject.repository;

import com.example.oneinkedoneproject.domain.Chat;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRepository extends JpaRepository<Chat,String > {
}
