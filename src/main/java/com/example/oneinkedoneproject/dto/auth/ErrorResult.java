package com.example.oneinkedoneproject.dto.auth;


public class ErrorResult {
    private String error;
    private String message;

    public ErrorResult(String error, String message) {
        this.error = error;
        this.message = message;
    }

    // Getters and setters
    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}