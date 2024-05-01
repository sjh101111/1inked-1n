package com.example.oneinkedoneproject.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Chat {
    @Id
    @Column(name = "chat_id" , nullable = false)
    private String id;

    @Column(name = "contents", nullable = false)
    private String contents;

    @Column(name = "sent_at", nullable = false)
    @CreatedDate
    private LocalDateTime sendAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id")
    private User sendUser;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receiver_id")
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

}
