package com.example.oneinkedoneproject.repository.comment;

import com.example.oneinkedoneproject.domain.Article;
import com.example.oneinkedoneproject.domain.Comment;
import com.example.oneinkedoneproject.domain.PasswordQuestion;
import com.example.oneinkedoneproject.domain.User;
import com.example.oneinkedoneproject.repository.article.ArticleRepository;
import com.example.oneinkedoneproject.repository.password.PasswordRepository;
import com.example.oneinkedoneproject.repository.user.UserRepository;
import com.example.oneinkedoneproject.utils.GenerateIdUtils;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.parameters.P;

import java.time.LocalDateTime;

@DataJpaTest
class CommentRepositoryUnitTest {
    @Autowired
    PasswordRepository passwordRepository;

    @Autowired
    CommentRepository commentRepository;

    @Autowired
    ArticleRepository articleRepository;

    @Autowired
    UserRepository userRepository;

    private PasswordQuestion passwordQuestion;
    private User user;

    private Article article;

    @BeforeEach
    void init(){
        passwordQuestion = new PasswordQuestion("1","질문");
        user = User.builder()
                .id(GenerateIdUtils.generateUserId())
                .passwordQuestion(passwordQuestion)
                .realname("test")
                .email("test@test.com")
                .password("1234")
                .withdraw(false)
                .build();
        article = Article.builder()
            .id(GenerateIdUtils.generateArticleId())
            .contents("blahblahblah")
            .createdAt(LocalDateTime.now())
            .updatedAt(LocalDateTime.now())
            .user(user)
            .build();

        passwordRepository.save(passwordQuestion);
        userRepository.save(user);
        articleRepository.save(article);
    }


    @Test
    void test(){
        Comment comment1 = Comment.builder()
                .comments("1")
                .id(GenerateIdUtils.generateCommentId())
                .user(user)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .article(article)
                .build();

        Comment comment2 = Comment.builder()
                .comments("2")
                .id(GenerateIdUtils.generateCommentId())
                .parent(comment1)
                .user(user)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .article(article)
                .build();

        Comment comment3 = Comment.builder()
                .comments("3")
                .id(GenerateIdUtils.generateCommentId())
                .user(user)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .parent(comment1)
                .article(article)
                .build();

        Comment comment4 = Comment.builder()
                .comments("4")
                .id(GenerateIdUtils.generateCommentId())
                .user(user)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .parent(comment3)
                .article(article)
                .build();

        commentRepository.save(comment1);
        commentRepository.save(comment2);
        commentRepository.save(comment3);
        commentRepository.save(comment4);

        Comment comment = commentRepository.findById(comment1.getId()).get();

        System.out.println(comment.getComments());
        System.out.print(comment.getComments() + "->");
        for (Comment child : comment.getReplyList()) {
            System.out.println(child.getComments());
            System.out.print(child.getComments() + "->");
            for (Comment grandchild : child.getReplyList()) {
                System.out.println(grandchild.getComments());
            }
        }

    }

}