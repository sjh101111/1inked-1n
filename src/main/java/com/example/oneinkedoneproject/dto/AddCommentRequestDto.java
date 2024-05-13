package com.example.oneinkedoneproject.dto;

import com.example.oneinkedoneproject.domain.Article;
import com.example.oneinkedoneproject.domain.Comment;
import com.example.oneinkedoneproject.domain.User;
import com.example.oneinkedoneproject.utils.GenerateIdUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AddCommentRequestDto {
    private String comments;
    private String parentId;

    public Comment toEntity(User user, Article article, Comment parentComment) {
        return Comment.builder()
                .id(GenerateIdUtils.generateCommentId())
                .comments(comments)
                .parent(parentComment)
                .user(user)
                .article(article)
                .build();
    }
}
