package com.example.oneinkedoneproject.dto.chat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class AddChatRequestDto {
    private String partnerEmail;
    private String contents;
}
