package com.example.oneinkedoneproject.dto.comment;


import java.time.LocalDateTime;

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
	private byte[] userProfileImage;
	private String realname;
	private String email;
    
    public CommentResponseDto(Comment comment){
    	id = comment.getId();
    	comments = comment.getComments();
    	createdAt = comment.getCreatedAt();
    	updatedAt = comment.getUpdatedAt();
		userProfileImage = comment.getUser().getImage();
		realname = comment.getUser().getRealname();
		email = comment.getUser().getEmail();
    	if (comment.getParent() != null)
    		parentId = comment.getParent().getId();
    	else
    		parentId = null;
    }
}
