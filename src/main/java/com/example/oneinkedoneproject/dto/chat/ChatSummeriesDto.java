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
public class ChatSummeriesDto {
    private String id;
    private String lastMessage;
    private LocalDateTime lastMessageAt;
    private User partenr;
    private User me;
    private String email;
    private boolean isDeleted;
}
