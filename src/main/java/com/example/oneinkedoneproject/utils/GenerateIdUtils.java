package com.example.oneinkedoneproject.utils;

import java.util.UUID;

public class GenerateIdUtils {
    public static String generateArticleId() {
        return "article_" + generateId();
    }

    public static String generateImageId() {
        return "image_" + generateId();
    }

    public static String generateUserId() {
        return "user_" + generateId();
    }

    public static String generateCommentId() {
        return "comment_" + generateId();
    }

    public static String generateResumeId() {
        return "resume_" + generateId();
    }

    public static String generateChatId() {return "chat_" + generateId(); }

    public static String generateId() {
        UUID uuid = UUID.randomUUID();
        // "-" 제외하고 문자열 추출
        String extractedString = uuid.toString().replaceAll("-", "");
        // 특정 문자만 추출 (예: 숫자만 추출)
        String extractedNumbers = extractedString.replaceAll("[^0-9]", "");

        return extractedNumbers;
    }
}
