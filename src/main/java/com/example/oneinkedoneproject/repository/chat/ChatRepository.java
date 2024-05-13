package com.example.oneinkedoneproject.repository.chat;

import com.example.oneinkedoneproject.domain.Chat;
import com.example.oneinkedoneproject.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface ChatRepository extends JpaRepository<Chat,String > {
    // 내가 보낸 메시지
    List<Chat> findAllBySendUser(User sender);

    // 내가 받은 메시지
    List<Chat> findByReceiverUser(User receiver);

    List<Chat> findAllBySendUserAndReceiverUser(User sendUser, User receiverUser);

    @Query("SELECT u FROM User u WHERE u.id IN (SELECT c.receiverUser.id FROM Chat c WHERE c.sendUser.id = :userId " +
            "UNION SELECT c.sendUser.id FROM Chat c WHERE c.receiverUser.id = :userId)")
    Set<User> findAllUniqueChatPartners(@Param("userId") String userId);
}
