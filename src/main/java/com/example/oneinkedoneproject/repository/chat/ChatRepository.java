package com.example.oneinkedoneproject.repository.chat;

import com.example.oneinkedoneproject.domain.Chat;
import com.example.oneinkedoneproject.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatRepository extends JpaRepository<Chat,String > {
    // 내가 보낸 메시지
    List<Chat> findBySendUser(User sender);

    // 내가 받은 메시지
    List<Chat> findByReceiverUser(User receiver);

    List<Chat> findAllBySendUserAndReceiverUser(User sendUser, User receiverUser);
}
