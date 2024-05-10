package com.example.oneinkedoneproject.dto.chat;

import com.example.oneinkedoneproject.domain.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class ChatResponseDto {
    private String id;
    private String contents;
    private boolean isDeletedTo;
    private boolean isDeletedFrom;
    private LocalDateTime sendAt;
    private User sendUser;
    private User receiveUser;

    //isSomething 불리언 값 직렬화 시 getter가 is를 빼서 반환하는 문제 해결
    public boolean getIsDeletedTo() {
        return this.isDeletedTo;
    }

    public boolean getIsDeletedFrom() {
        return this.isDeletedFrom;
    }
}
