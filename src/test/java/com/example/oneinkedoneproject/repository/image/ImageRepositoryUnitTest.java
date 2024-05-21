package com.example.oneinkedoneproject.repository.image;

import com.example.oneinkedoneproject.OneinkedOneProjectApplication;
import com.example.oneinkedoneproject.domain.Article;
import com.example.oneinkedoneproject.domain.Image;
import com.example.oneinkedoneproject.domain.PasswordQuestion;
import com.example.oneinkedoneproject.domain.User;
import com.example.oneinkedoneproject.repository.article.ArticleRepository;
import com.example.oneinkedoneproject.repository.password.PasswordRepository;
import com.example.oneinkedoneproject.repository.user.UserRepository;
import com.example.oneinkedoneproject.utils.GenerateIdUtils;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ContextConfiguration;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ImageRepositoryUnitTest {

    @Autowired
    ImageRepository imageRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private PasswordRepository passwordRepository;

    public static User user;
    public static Image image;
    public static Article article;

    private PasswordQuestion passwordQuestion;

    @BeforeEach
    void setUp() {
        passwordQuestion = PasswordQuestion
                .builder()
                .id("1")
                .question("질문")
                .build();

        user = User.builder()
                .id(GenerateIdUtils.generateUserId())
                .realname("test")
                .email("test@test.com")
                .passwordQuestion(passwordQuestion)
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

        byte[] bytes = new byte[1024];

        image = Image.builder()
                .id(GenerateIdUtils.generateImageId())
                .article(article)
                .img(bytes)
                .build();

        passwordRepository.save(passwordQuestion);
        userRepository.save(user);
    }

    @Test
    @Order(1)
    @DisplayName("이미지 생성")
    void createImage() {
        // given
        Article savedArticle = articleRepository.save(article);

        // when
        Image savedImage = imageRepository.save(image);

        // then
        assertThat(savedImage).isNotNull();
        assertThat(savedImage.getId()).isEqualTo(image.getId());
    }

    @Test
    @Order(2)
    @DisplayName("이미지 수정")
    void updateImage() {
        // given
        Article savedArticle = articleRepository.save(article);
        Image savedImage = imageRepository.save(image);

        // when
        byte[] bytes = new byte[2048];
        savedImage.update(bytes);

        // then
        Image findImage = imageRepository.findById(savedImage.getId()).get();
        assertThat(findImage).isNotNull();
        assertThat(savedImage.getImg()).isEqualTo(findImage.getImg());
    }

    @Test
    @Order(3)
    @DisplayName("이미지 삭제")
    void deleteImage() {
        // given
        Article savedArticle = articleRepository.save(article);
        imageRepository.save(image);

        // when
        imageRepository.delete(image);
        List<Image> images = imageRepository.findAll();

        // then
        assertThat(images).isEmpty();
    }

}