package com.example.oneinkedoneproject.service;

import com.example.oneinkedoneproject.OneinkedOneProjectApplication;
import com.example.oneinkedoneproject.domain.Article;
import com.example.oneinkedoneproject.domain.Comment;
import com.example.oneinkedoneproject.domain.User;
import com.example.oneinkedoneproject.dto.AddCommentRequestDto;
import com.example.oneinkedoneproject.dto.UpdateCommentRequestDto;
import com.example.oneinkedoneproject.repository.article.ArticleRepository;
import com.example.oneinkedoneproject.repository.comment.CommentRepository;
import com.example.oneinkedoneproject.repository.UserRepository;
import com.example.oneinkedoneproject.service.comment.CommentService;
import com.example.oneinkedoneproject.utils.GenerateIdUtils;
import org.assertj.core.api.Assertions;
import org.hibernate.sql.Update;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ContextConfiguration;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.InstanceOfAssertFactories.optional;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class CommentServiceUnitTest {
    @Mock
    private CommentRepository commentRepository;

    @Mock
    private ArticleRepository articleRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private CommentService commentService;

    private String comments;

    @BeforeEach
    void setup() {
        comments = "댓글 내용";
    }

    @Test
    @DisplayName("댓글 생성")
    void saveComment() {
        // given
        doReturn(Optional.of(Article.builder()
                .id(GenerateIdUtils.generateArticleId())
                .updatedAt(LocalDateTime.now())
                .createdAt(LocalDateTime.now())
                .contents("게시글 내용")
                .user(null)
                .build())
        ).when(articleRepository).findById(any(String.class));

        AddCommentRequestDto addCommentRequestDto = new AddCommentRequestDto(comments);

        Comment comment = Comment.builder()
                .id(GenerateIdUtils.generateCommentId())
                .article(null)
                .comments(comments)
                .user(null)
                .build();


        doReturn(comment).when(commentRepository).save(any(Comment.class));

        // when
        Comment savedComment = commentService.save(null, "123", addCommentRequestDto);

        // then
        assertThat(savedComment).isNotNull();
        assertThat(savedComment.getComments()).isEqualTo(comments);
    }

    @Test
    @DisplayName("댓글 생성 실패-articleId를 못찾는 경우")
    void saveCommentFail() {
        // given
        doReturn(Optional.empty()).when(articleRepository).findById(any(String.class));

        AddCommentRequestDto addCommentRequestDto = new AddCommentRequestDto(comments);

        // when
        assertThatThrownBy(() -> commentService.save(null, "123", addCommentRequestDto)).isInstanceOf(IllegalArgumentException.class);
        // then
    }

    @Test
    @DisplayName("댓글 조회")
    void readComment() {
        // given
        Comment comment1 = Comment.builder()
                .id(GenerateIdUtils.generateCommentId())
                .article(null)
                .comments(comments)
                .user(null)
                .build();

        Comment comment2 = Comment.builder()
                .id(GenerateIdUtils.generateCommentId())
                .article(null)
                .parent(comment1)
                .comments(comments)
                .user(null)
                .build();

        Comment comment3 = Comment.builder()
                .id(GenerateIdUtils.generateCommentId())
                .article(null)
                .parent(comment1)
                .comments(comments)
                .user(null)
                .build();

        Comment comment4 = Comment.builder()
                .id(GenerateIdUtils.generateCommentId())
                .article(null)
                .comments(comments)
                .parent(comment2)
                .user(null)
                .build();

        String articleId = "1";

        doReturn(Optional.of(Article.builder()
                .id("1")
                .user(null)
                .contents("게시글 내용")
                .commentList(List.of(comment1, comment2, comment3, comment4))
                .build())).when(articleRepository).findById(articleId);


        // when
        List<Comment> rootCommentList = commentService.getRootComments(articleId);
        // then
        assertThat(rootCommentList.size()).isEqualTo(1);
        assertThat(rootCommentList.get(0).getComments()).isEqualTo(comments);
    }

    @Test
    @DisplayName("댓글 수정")
    void updateComment() {
        // given
        Comment before = Comment.builder()
                .id(GenerateIdUtils.generateCommentId())
                .article(null)
                .comments(comments)
                .user(null)
                .build();
        UpdateCommentRequestDto updateCommentRequestDto = new UpdateCommentRequestDto("바뀐내용");
        doReturn(Optional.of(before)).when(commentRepository).findById(before.getId());

        // when
        Comment after = commentService.updateComment(before.getId(), updateCommentRequestDto);

        // then
        assertThat(after.getComments()).isEqualTo("바뀐내용");
    }

    @Test
    @DisplayName("댓글 수정 실패")
    void updateCommentFail() {
        // given
        doReturn(Optional.empty()).when(commentRepository).findById(any(String.class));
        UpdateCommentRequestDto updateCommentRequestDto = new UpdateCommentRequestDto("엥?");

        // when
        // then
        assertThatThrownBy(() -> commentService.updateComment("1", updateCommentRequestDto)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("댓글 삭제")
    void deleteComment() {
        // given
        doNothing().when(commentRepository).deleteById(any(String.class));

        // when
        commentService.deleteComment("1");

        // then
        verify(commentRepository, only()).deleteById(any(String.class));
    }


}
