package com.example.oneinkedoneproject.dto.auth;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class ErrorResult {
    private String error;
    private String message;

    // Getters and setters
    public void setError(String error) {
        this.error = error;
    }
    public void setMessage(String message) {
        this.message = message;
    }
}