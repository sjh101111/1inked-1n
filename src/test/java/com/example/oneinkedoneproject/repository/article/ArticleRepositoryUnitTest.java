package com.example.oneinkedoneproject.repository.article;

import com.example.oneinkedoneproject.OneinkedOneProjectApplication;
import com.example.oneinkedoneproject.domain.Article;
import com.example.oneinkedoneproject.domain.User;
import com.example.oneinkedoneproject.repository.user.UserRepository;
import com.example.oneinkedoneproject.utils.GenerateIdUtils;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ContextConfiguration;

import java.util.List;


import static org.assertj.core.api.Assertions.*;

@DataJpaTest
@ContextConfiguration(classes = OneinkedOneProjectApplication.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ArticleRepositoryUnitTest {
    @Autowired
    ArticleRepository articleRepository;

    @Autowired
    UserRepository userRepository;

    private static Article article;
    private static User user;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .id(GenerateIdUtils.generateUserId())
                .username("test")
                .email("test@test.com")
                .password("test")
                .withdraw(false)
                .build();

        article = Article.builder()
                .id(GenerateIdUtils.generateArticleId())
                .contents("test content")
                .user(user)
                .updatedAt(null)
                .createdAt(null)
                .build();
    }


    @Test
    @DisplayName("게시글 생성")
    @Order(1)
    void createArticle() {
        // given
        // static article 사용

        // when
        Article savedArticle = articleRepository.save(article);

        // then
        assertThat(savedArticle).isNotNull();
        assertThat(savedArticle.getId()).isEqualTo(article.getId());

    }

    @Test
    @DisplayName("게시판 목록 조회")
    @Order(2)
    void readArticleList() {
        // given
        User savedUser = userRepository.save(user);
        Article savedArticle = articleRepository.save(article);

        // when
        List<Article> articleList = articleRepository.findAll();

        // then
        assertThat(articleList).isNotEmpty();
        assertThat(articleList.size()).isEqualTo(1);
    }

    @Test
    @Order(3)
    @DisplayName("게시글 수정")
    void updateArticle() {
        // given
        Article savedArticle = articleRepository.save(article);

        // when
        savedArticle.update("updated content");

        //then
        Article findArticle = articleRepository.findById(savedArticle.getId()).get();
        assertThat(findArticle).isNotNull();
        assertThat(findArticle.getContents()).isEqualTo("updated content");
    }

    @Test
    @Order(4)
    @DisplayName("게시글 삭제")
    void deleteArticle() {
        // given
        articleRepository.save(article);

        // when
        articleRepository.delete(article);
        List<Article> articleList = articleRepository.findAll();

        // then
        assertThat(articleList).isEmpty();
    }
}
