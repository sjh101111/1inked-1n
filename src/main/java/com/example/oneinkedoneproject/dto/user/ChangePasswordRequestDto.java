package com.example.oneinkedoneproject.dto.user;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ChangePasswordRequestDto {
    private String email;
    private String passwordQuestionId;
    private String passwordQuestionAnswer;
    private String newPassword;
}
