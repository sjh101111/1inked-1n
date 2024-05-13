package com.example.oneinkedoneproject.domain;

import com.example.oneinkedoneproject.dto.chat.ChatResponseDto;
import com.example.oneinkedoneproject.utils.GenerateIdUtils;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Getter
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor
public class Chat {
    @Id
    @Builder.Default
    @Column(name = "chat_id" , nullable = false)
    private String id = GenerateIdUtils.generateChatId();

    @Column(name = "contents", nullable = false)
    private String contents;

    @Column(name = "sent_at", nullable = false)
    @CreatedDate
    private LocalDateTime sendAt;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "sender_id", nullable = false)
    private User sendUser;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "receiver_id", nullable = false)
    private User receiverUser;

    @Column(name = "is_deleted_to")
    private boolean isDeletedTo;

    @Column(name = "is_deleted_from")
    private boolean isDeletedFrom;

    public Chat(String id, String contents, User sendUser, User receiverUser) {
        this.id = id;
        this.contents = contents;
        this.sendAt = LocalDateTime.now();
        this.sendUser = sendUser;
        this.receiverUser = receiverUser;
        this.isDeletedTo = false;
        this.isDeletedFrom = false;
    }

    public void updateContents(String contents){
        this.contents = contents;
    }

    public void isDeletedToUpdate(boolean isDeletedTo) {
        this.isDeletedTo = isDeletedTo;
    }

    public void isDeletedFromUpdate(boolean isDeletedFrom) {
        this.isDeletedFrom = isDeletedFrom;
    }

    public ChatResponseDto toDto() {
        return ChatResponseDto.builder().id(id).contents(contents).isDeletedFrom(isDeletedFrom)
                .isDeletedTo(isDeletedTo).sendAt(sendAt)
                .sendUser(sendUser).receiveUser(receiverUser).
                build();
    }

}
