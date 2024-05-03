package com.example.oneinkedoneproject.dto.auth;


import com.example.oneinkedoneproject.domain.PasswordQuestion;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

//회원가입을 위한 request
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {

    private String realname;
    private String email;
    private String password;
    private PasswordQuestion passwordQuestion;
    private String passwordAnswer;
}
