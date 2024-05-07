package com.example.oneinkedoneproject.repository.chat;

import com.example.oneinkedoneproject.domain.Chat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatRepository extends JpaRepository<Chat,String > {
}
