package com.example.oneinkedoneproject.dto.auth;



import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ErrorResult {
    private String error;
    private String message;
}