package com.example.oneinkedoneproject.controller;

import com.example.oneinkedoneproject.domain.*;
import com.example.oneinkedoneproject.dto.article.AddArticleRequestDto;
import com.example.oneinkedoneproject.repository.article.ArticleRepository;
import com.example.oneinkedoneproject.repository.image.ImageRepository;
import com.example.oneinkedoneproject.repository.user.UserRepository;
import com.example.oneinkedoneproject.service.article.ArticleService;
import com.example.oneinkedoneproject.service.image.ImageService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.DefaultMockMvcBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
public class ArticleIntegratedTest {
    @Autowired
    private MockMvc mvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    ArticleRepository articleRepository;

    @Autowired
    ImageRepository imageRepository;

    @Autowired
    ArticleService articleService;

    @Autowired
    ImageService imageService;

    private User user;

    private Article article;

    private List<Image> images;

    private Follow follow;

    MockMultipartFile file = new MockMultipartFile("file", "filename.txt", "text/plain", "some content".getBytes());
    List<MultipartFile> files = List.of(file);
    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        this.mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).
                apply(SecurityMockMvcConfigurers.springSecurity()).build();

        user = User.builder()
                .withdraw(false)
                .email("test@test.com")
                .password("1234")
                .id("1")
                .realname("name")
                .grade(Grade.ROLE_BASIC)
                .passwordQuestion(PasswordQuestion.builder()
                        .id("1")
                        .question("?")
                        .build())
                .build();
        Authentication auth = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(auth);
    }

    @AfterEach
    void afterSetUp() {
        imageRepository.deleteAll();
        articleRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void createArticleTest() throws Exception {
        MockMultipartFile image1 = new MockMultipartFile(
                "files",
                "image1.png",
                MediaType.IMAGE_PNG_VALUE,
                "image content 1".getBytes()
        );

        MockMultipartFile image2 = new MockMultipartFile(
                "files",
                "image2.jpg",
                MediaType.IMAGE_JPEG_VALUE,
                "image content 2".getBytes()
        );

        // Test sending title field
        ResultActions resultActions = mvc.perform(
                multipart("/api/article")
                        .file(image1)
                        .file(image2)
                        .param("contents", "create content")
                        .with(request -> {
                            request.setMethod("POST");
                            return request;
                        })
        ).andExpect(status().isCreated());
        Article createdArticle = articleRepository.findByUserId(user.getId());

        assertThat(createdArticle.getImageList().size()).isEqualTo(2);
    }

    @Test
    void updateArticleTest() throws Exception {
        images = new ArrayList<>();
        for (MultipartFile fileImage : files) {
            Image image = Image.builder()
                    .img(fileImage.getBytes())
                    .build();
            images.add(image);
        }
        Article article = articleRepository.save(Article.builder()
                .id("1")
                .contents("게시글 내용")
                .user(user)
                .imageList(images)
                .build());

        MockMultipartFile image1 = new MockMultipartFile(
                "files",
                "image1.png",
                MediaType.IMAGE_PNG_VALUE,
                "image content 1".getBytes()
        );

        MockMultipartFile image2 = new MockMultipartFile(
                "files",
                "image2.jpg",
                MediaType.IMAGE_JPEG_VALUE,
                "image content 2".getBytes()
        );

        List<MultipartFile> updatedfiles = new ArrayList<>();
        updatedfiles.add(image1);
        updatedfiles.add(image2);

        ResultActions resultActions = mvc.perform(multipart("/api/article/{articleId}", article.getId())
                        .file(image1)
                        .file(image2)
                        .param("contents", "updated contents")
                        .with(request -> {
                            request.setMethod("PUT");
                            return request;
                        })
                ).andExpect(status().isOk()).
                andExpect(jsonPath("contents").value("updated contents"))
                .andDo(print());

        Article updatedArticle = articleRepository.findById(article.getId())
                .orElseThrow();

        assertThat(updatedArticle.getImageList()).isEqualTo(3);
    }

    @Test
    void readMyAllArticlesTest() throws Exception {
        images = new ArrayList<>();
        for (MultipartFile fileImage : files) {
            Image image = Image.builder()
                    .img(fileImage.getBytes())
                    .build();
            images.add(image);
        }
        Article article = articleRepository.save(Article.builder()
                .id("1")
                .contents("게시글 내용")
                .user(user)
                .imageList(images)
                .build());

        ResultActions resultActions = mvc.perform(get("/api/myAllArticles"));

        resultActions.andExpect(jsonPath("$[0].contents").value("게시글 내용"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].imageList.size()").value(1));
    }

    @Test
    void readMainFeedArticlesTest() throws Exception {
        User toUser = userRepository.save(User.builder()
                .withdraw(false)
                .email("test@test.com")
                .password("1234")
                .id("2")
                .realname("toUsername")
                .grade(Grade.ROLE_BASIC)
                .passwordQuestion(PasswordQuestion.builder()
                        .id("1")
                        .question("?")
                        .build())
                .build());

        images = new ArrayList<>();
        for (MultipartFile fileImage : files) {
            Image image = Image.builder()
                    .img(fileImage.getBytes())
                    .build();
            images.add(image);
        }

        Article article = articleRepository.save(Article.builder()
                .id("1")
                .contents("게시글 내용")
                .user(user)
                .imageList(images)
                .build());

        follow = new Follow("1", toUser, user);

        ResultActions resultActions = mvc.perform(get("/api/mainFeedArticles"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].contents").value("게시글 내용"))
                .andExpect(jsonPath("$[0].user").value(toUser))
                .andDo(print());
    }

    @Test
    void deleteArticle() throws Exception {
        Article article = articleRepository.save(Article.builder()
                .id("1")
                .contents("게시글 내용")
                .user(user)
                .imageList(images)
                .build());

        ResultActions resultActions = mvc.perform(delete("/api/article/{articleId}", article.getId()))
                .andExpect(status().isOk())
                .andDo(print());

        Optional<Article> deleteVerify = articleRepository.findById(article.getId());

        Assertions.assertFalse(deleteVerify.isPresent());
    }
}
