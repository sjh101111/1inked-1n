package com.example.oneinkedoneproject.repository.image;

import com.example.oneinkedoneproject.OneinkedOneProjectApplication;
import com.example.oneinkedoneproject.domain.Article;
import com.example.oneinkedoneproject.domain.Image;
import com.example.oneinkedoneproject.domain.User;
import com.example.oneinkedoneproject.repository.article.ArticleRepository;
import com.example.oneinkedoneproject.utils.GenerateIdUtils;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ContextConfiguration;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ContextConfiguration(classes = OneinkedOneProjectApplication.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ImageRepositoryUnitTest {

    @Autowired
    ImageRepository imageRepository;

    @Autowired
    private ArticleRepository articleRepository;

    public static User user;
    public static Image image;
    public static Article article;

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

        byte[] bytes = new byte[1024];

        image = Image.builder()
                .id(GenerateIdUtils.generateImageId())
                .article(article)
                .img(bytes)
                .build();
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