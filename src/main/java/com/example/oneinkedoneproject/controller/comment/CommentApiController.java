package com.example.oneinkedoneproject.controller.comment;

import com.example.oneinkedoneproject.domain.Comment;
import com.example.oneinkedoneproject.domain.User;
import com.example.oneinkedoneproject.dto.AddCommentRequestDto;
import com.example.oneinkedoneproject.dto.CommentResponseDto;
import com.example.oneinkedoneproject.dto.UpdateCommentRequestDto;
import com.example.oneinkedoneproject.service.comment.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class CommentApiController {
    private final CommentService commentService;

    @PostMapping("/api/comment/{articleId}")
    public ResponseEntity<Comment> addComment(
            @AuthenticationPrincipal User user,
            @PathVariable(value = "articleId") String articleId,
            @RequestBody AddCommentRequestDto requestDto) {
        Comment savedComment = commentService.save(user, articleId, requestDto);
        if (savedComment == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(savedComment);

    }

    @GetMapping("/api/comment/{articleId}")
    public ResponseEntity<List<CommentResponseDto>> readComment(
            @PathVariable(value = "articleId") String articleId) {
        List<Comment> commentList = commentService.getComments(articleId);
        if(commentList == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        List<CommentResponseDto> result = new ArrayList<>();
        for(Comment c : commentList) {
        	result.add(new CommentResponseDto(c));
        }
        return ResponseEntity.ok().body(result);
    }

    @PatchMapping("/api/comment/{commentId}")
    public ResponseEntity<Comment> updateComment(
            @PathVariable(value = "commentId") String commentId,
            @RequestBody UpdateCommentRequestDto requestDto
    ) {
        Comment updateComment = commentService.updateComment(commentId, requestDto);
        if (updateComment == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        return ResponseEntity.ok().body(updateComment);
    }

    @DeleteMapping("/api/comment/{commentId}")
    public ResponseEntity<Void> deleteComment(
            @PathVariable(value = "commentId") String commentId) {
        commentService.deleteComment(commentId);
        return ResponseEntity.ok().body(null);
    }
}
