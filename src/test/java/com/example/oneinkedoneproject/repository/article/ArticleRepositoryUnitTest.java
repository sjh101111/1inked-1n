package com.example.oneinkedoneproject.repository.article;

import com.example.oneinkedoneproject.OneinkedOneProjectApplication;
import com.example.oneinkedoneproject.domain.Article;
import com.example.oneinkedoneproject.domain.PasswordQuestion;
import com.example.oneinkedoneproject.domain.User;
import com.example.oneinkedoneproject.repository.password.PasswordRepository;
import com.example.oneinkedoneproject.repository.user.UserRepository;
import com.example.oneinkedoneproject.utils.GenerateIdUtils;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ContextConfiguration;

import java.util.List;


import static org.assertj.core.api.Assertions.*;

@DataJpaTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ArticleRepositoryUnitTest {
    @Autowired
    ArticleRepository articleRepository;

	@Autowired
	PasswordRepository passwordRepository;

    @Autowired
    UserRepository userRepository;

	private PasswordQuestion passwordQuestion;
    private Article article;
    private User user;

    @BeforeEach
    void setUp() {
		passwordQuestion = new PasswordQuestion("1", "질문");
        passwordRepository.save(passwordQuestion);
        user = User.builder()
                .id(GenerateIdUtils.generateUserId())
                .realname("test")
                .email("test@test.com")
                .password("test")
				.passwordQuestion(passwordQuestion)
                .withdraw(false)
                .build();

        article = Article.builder()
                .id(GenerateIdUtils.generateArticleId())
                .contents("test content")
                .user(user)
                .updatedAt(null)
                .createdAt(null)
                .build();
        userRepository.save(user);
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
