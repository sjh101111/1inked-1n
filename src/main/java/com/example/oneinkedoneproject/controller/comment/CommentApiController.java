package com.example.oneinkedoneproject.controller.comment;

import com.example.oneinkedoneproject.domain.Comment;
import com.example.oneinkedoneproject.domain.User;
import com.example.oneinkedoneproject.dto.comment.AddCommentRequestDto;
import com.example.oneinkedoneproject.dto.comment.CommentResponseDto;
import com.example.oneinkedoneproject.dto.comment.UpdateCommentRequestDto;
import com.example.oneinkedoneproject.service.comment.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Tag(name = "Comment", description = "댓글 API")
public class CommentApiController {
    private final CommentService commentService;

    @PostMapping("/api/comment/{articleId}")
    @Operation(summary = "댓글 생성", description = "게시글에 댓글을 생성하는 API")
    @Parameters({
            @Parameter(name = "articleId", description = "게시물 ID", example = "article_{UUID}"),
            @Parameter(name = "comments", description = "댓글 내용", example = "댓글 내용 예시"),
            @Parameter(name = "parentId", description = "부모 댓글 ID", example = "comment_{UUID}")
    })
    @ApiResponse(responseCode = "201", description = "요청에 성공하셨습니다.", content = @Content(mediaType = "application/json"))
    public ResponseEntity<CommentResponseDto> addComment(
            @AuthenticationPrincipal User user,
            @PathVariable(value = "articleId") String articleId,
            @RequestBody AddCommentRequestDto requestDto) {
        Comment savedComment = commentService.save(user, articleId, requestDto);
        if (savedComment == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(new CommentResponseDto(savedComment));

    }


    @GetMapping("/api/comment/{articleId}")
    @Operation(summary = "댓글 조회", description = "게시글의 댓글을 읽어오는 API")
    @Parameters({
            @Parameter(name = "articleId", description = "게시물 ID", example = "article_{UUID}")
    })
    @ApiResponse(responseCode = "200", description = "요청에 성공하셨습니다.", content = @Content(mediaType = "application/json"))
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
    @Operation(summary = "댓글 수정", description = "작성한 댓글을 수정하는 API")
    @Parameters({
            @Parameter(name = "commentId", description = "수정할 댓글 ID", example = "comment_{UUID}"),
            @Parameter(name = "comments", description = "댓글 내용", example = "댓글 내용 예시")
    })
    @ApiResponse(responseCode = "200", description = "요청에 성공하셨습니다.", content = @Content(mediaType = "application/json"))
    public ResponseEntity<CommentResponseDto> updateComment(
            @PathVariable(value = "commentId") String commentId,
            @RequestBody UpdateCommentRequestDto requestDto
    ) {
        Comment updateComment = commentService.updateComment(commentId, requestDto);
        if (updateComment == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        return ResponseEntity.ok().body(new CommentResponseDto(updateComment));
    }

    @DeleteMapping("/api/comment/{commentId}")
    @Operation(summary = "댓글 삭제", description = "작성된 댓글을 삭제하는 API")
    @Parameters({
            @Parameter(name = "commentId", description = "삭제할 댓글 ID", example = "comment_{UUID}"),
    })
    @ApiResponse(responseCode = "200", description = "요청에 성공하셨습니다.")
    public ResponseEntity<Void> deleteComment(
            @PathVariable(value = "commentId") String commentId) {
        commentService.deleteComment(commentId);
        return ResponseEntity.ok().build();
    }
}
