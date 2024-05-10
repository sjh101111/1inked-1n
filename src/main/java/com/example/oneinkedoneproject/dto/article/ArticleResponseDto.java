package com.example.oneinkedoneproject.dto.article;

import com.example.oneinkedoneproject.domain.Comment;
import com.example.oneinkedoneproject.domain.Image;
import com.example.oneinkedoneproject.domain.User;
import com.example.oneinkedoneproject.dto.image.ImageResponseDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
    private List<ImageResponseDto> images;
    private User user;
    private List<Comment> comments;
}
