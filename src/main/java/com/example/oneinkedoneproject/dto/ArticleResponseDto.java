package com.example.oneinkedoneproject.dto;

import com.example.oneinkedoneproject.domain.Article;

import com.example.oneinkedoneproject.domain.Comment;
import com.example.oneinkedoneproject.domain.Image;
import com.example.oneinkedoneproject.domain.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ArticleResponseDto {
    private String id;
    private String contents;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<Image> images;
    private User user;
    private List<Comment> comments;
}
