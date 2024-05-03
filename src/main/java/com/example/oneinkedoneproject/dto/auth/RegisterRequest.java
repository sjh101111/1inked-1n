package com.example.oneinkedoneproject.dto.auth;


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
    private String passwordQuestion;
    private String passwordAnswer;
}
