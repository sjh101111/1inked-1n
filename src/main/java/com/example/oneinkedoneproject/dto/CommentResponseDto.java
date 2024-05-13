package com.example.oneinkedoneproject.dto;


import java.time.LocalDateTime;
import java.util.List;

import com.example.oneinkedoneproject.domain.Comment;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommentResponseDto {
    private String id;
    private String comments;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String parentId;
    
    public CommentResponseDto(Comment comment){
    	id = comment.getId();
    	comments = comment.getComments();
    	createdAt = comment.getCreatedAt();
    	updatedAt = comment.getUpdatedAt();
    	if (comment.getParent() != null)
    		parentId = comment.getParent().getId();
    	else
    		parentId = null;
    }
}
