package com.example.oneinkedoneproject.dto.chat;

import com.example.oneinkedoneproject.domain.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChatSummariesDto {
    private String id;
    private String lastMessage;
    private LocalDateTime lastMessageAt;
    private User partner;
    private User me;
    private boolean isDeleted;

    public boolean getIdDeleted() {
        return this.isDeleted;
    }
}
