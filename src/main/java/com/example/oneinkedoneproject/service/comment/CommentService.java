package com.example.oneinkedoneproject.service.comment;

import com.example.oneinkedoneproject.domain.Article;
import com.example.oneinkedoneproject.domain.Comment;
import com.example.oneinkedoneproject.domain.User;
import com.example.oneinkedoneproject.dto.AddCommentRequestDto;
import com.example.oneinkedoneproject.dto.UpdateCommentRequestDto;
import com.example.oneinkedoneproject.repository.article.ArticleRepository;
import com.example.oneinkedoneproject.repository.comment.CommentRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentService {

    private CommentRepository commentRepository;
    private ArticleRepository articleRepository;

    public Comment save(User user, String articleId, AddCommentRequestDto request) {
        Article article = articleRepository.findById(articleId).orElseThrow(() -> new IllegalArgumentException("not found: " + articleId));

        Comment comment = request.toEntity(user, article);

        return commentRepository.save(comment);
    }

    public List<Comment> getRootComments(String articleId) {
        Article article = articleRepository.findById(articleId).orElseThrow(() -> new IllegalArgumentException("not found: " + articleId));

        return article.getCommentList().stream().filter(c -> c.getParent() == null).toList();
    }

    @Transactional
    public Comment updateComment(String commentId, UpdateCommentRequestDto request) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new IllegalArgumentException("not found: " + commentId));

        comment.update(request.getComments());
        return comment;
    }

    public void deleteComment(String commentId) {
        commentRepository.deleteById(commentId);
    }
}
