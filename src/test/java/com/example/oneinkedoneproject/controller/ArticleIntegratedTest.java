package com.example.oneinkedoneproject.controller;

import com.example.oneinkedoneproject.domain.*;
import com.example.oneinkedoneproject.dto.article.AddArticleRequestDto;
import com.example.oneinkedoneproject.dto.article.ArticleResponseDto;
import com.example.oneinkedoneproject.dto.auth.TokenInfo;
import com.example.oneinkedoneproject.repository.article.ArticleRepository;
import com.example.oneinkedoneproject.repository.follow.FollowRepository;
import com.example.oneinkedoneproject.repository.image.ImageRepository;
import com.example.oneinkedoneproject.repository.password.PasswordRepository;
import com.example.oneinkedoneproject.repository.user.UserRepository;
import com.example.oneinkedoneproject.service.article.ArticleService;
import com.example.oneinkedoneproject.service.auth.JwtTokenProvider;
import com.example.oneinkedoneproject.service.image.ImageService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
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
import org.springframework.test.web.servlet.request.MockMultipartHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
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

    @Autowired
    PasswordRepository passwordRepository;

    @Autowired
    FollowRepository followRepository;

    private User user;

    private Article article;

    private List<Image> images;

    private Follow follow;

    private String accessToken;

    MockMultipartFile file = new MockMultipartFile("file", "filename.txt", "text/plain", "some content".getBytes());
    List<MultipartFile> files = List.of(file);
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @BeforeEach
    void setUp() {
        followRepository.deleteAll();
        imageRepository.deleteAll();
        articleRepository.deleteAll();
        userRepository.deleteAll();
        this.mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).
                apply(SecurityMockMvcConfigurers.springSecurity()).build();

        this.user = userRepository.save(User.builder()
                .withdraw(false)
                .email("test@test.com")
                .password("1234")
//                .id("1")
                .realname("name")
                .grade(Grade.ROLE_BASIC)
                .passwordQuestion(
                        passwordRepository.save(PasswordQuestion.builder()
//                                .id("1")
                                .question("?")
                                .build()))
                .build());
        Authentication auth = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());

        TokenInfo tokenInfo = jwtTokenProvider.createToken(auth);
//        String accessToken = tokenInfo.getAccessToken();

        // Set the JWT token in the security context (for internal use)
        SecurityContextHolder.getContext().setAuthentication(auth);

        // Store the accessToken for use in requests
        this.accessToken = tokenInfo.getAccessToken();
//        String accessToken = tokenInfo.getAccessToken();
    }

    @AfterEach
    void afterSetUp() {
//        imageRepository.deleteAll();
//        articleRepository.deleteAll();
//        userRepository.deleteAll();
    }

    @Test
//    @Transactional
    void createArticleTest() throws Exception {

        // MockMultipartFile를 사용하여 테스트 파일 생성
        MockMultipartFile imageFile = new MockMultipartFile(
                "files",             // 필드 이름 (Controller에서 지정한 이름과 일치해야 합니다)
                "test-image.jpg",      // 파일명
                "image/jpeg",          // 파일 타입
                "Test image content".getBytes()); // 파일의 내용

        // MockMvcRequestBuilders.multipart 사용하여 멀티파트 요청 구성
        MockMultipartHttpServletRequestBuilder builder =
                MockMvcRequestBuilders.multipart("/api/article");

        // Form data 추가
        builder.with(request -> {
            request.setMethod("POST");
            return request;
        });

        // 텍스트 필드 추가
        builder.param("contents", "aa");

        builder.file(imageFile);
        builder.header("Authorization", "Bearer " + accessToken);
        // MockMvc를 사용하여 요청 실행 및 검증
        ResultActions resultActions = mvc.perform(builder)
                .andExpect(status().isCreated())
                .andDo(print())
//                .andExpect(jsonPath("title").value("aa"))
                .andExpect(jsonPath("contents").value("aa"));

        ArticleResponseDto createdArticle = articleRepository.findByUserId(user.getId()).toDto();

        assertThat(createdArticle.getImages().size()).isEqualTo(1);
    }

    @Test
    void updateArticleTest() throws Exception {
        images = new ArrayList<>();
        article = Article.builder()
//                .id("1")
                .contents("게시글 내용")
                .user(user)
                .imageList(images)
                .build();
        for (MultipartFile fileImage : files) {
            Image image = Image.builder()
                    .img(fileImage.getBytes()).article(article)
                    .build();
            article.addImage(image);
        }

        articleRepository.save(article);

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
                        .header("Authorization", "Bearer " + accessToken)
                        .with(request -> {
                            request.setMethod("PUT");
                            return request;
                        })
                ).
                andExpect(status().isOk()).
                andExpect(jsonPath("contents").value("updated contents"))
                .andDo(print());

        Article updatedArticle = articleRepository.findById(article.getId())
                .orElseThrow();

        assertThat(updatedArticle.getImageList().size()).isEqualTo(2);
    }

    @Test
    void readMyAllArticlesTest() throws Exception {
        images = new ArrayList<>();
        article = Article.builder()
//                .id("1")
                .contents("게시글 내용")
                .user(user)
                .imageList(images)
                .build();
        for (MultipartFile fileImage : files) {
            Image image = Image.builder()
                    .img(fileImage.getBytes()).article(article)
                    .build();
            article.addImage(image);
        }

        articleRepository.save(article);

        ResultActions resultActions = mvc.perform(get("/api/myAllArticles")
                .header("Authorization", "Bearer " + accessToken))
                .andDo(print());

        resultActions.andExpect(jsonPath("$[0].contents").value("게시글 내용"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].images.size()").value(1));
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
                .passwordQuestion(
                        passwordRepository.save(PasswordQuestion.builder()
//                                .id("1")
                                .question("?")
                                .build()))
                .build());

        images = new ArrayList<>();
        article = Article.builder()
//                .id("1")
                .contents("게시글 내용")
                .user(toUser)
                .imageList(images)
                .build();
        for (MultipartFile fileImage : files) {
            Image image = Image.builder()
                    .img(fileImage.getBytes()).article(article)
                    .build();
            article.addImage(image);
        }

        articleRepository.save(article);

        follow = new Follow("1", toUser, user);
        followRepository.save(follow);
        ResultActions resultActions = mvc.perform(get("/api/mainFeedArticles")
                        .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].contents").value("게시글 내용"))
//                .andExpect(jsonPath("$[0].user").value(toUser))
                .andDo(print());
    }

    @Test
//    @Transactional
    void deleteArticle() throws Exception {
        images = new ArrayList<>();
        article = Article.builder()
//                .id("1")
                .contents("게시글 내용")
                .user(user)
                .imageList(images)
                .build();
        for (MultipartFile fileImage : files) {
            Image image = Image.builder()
                    .img(fileImage.getBytes()).article(article)
                    .build();
            article.addImage(image);
        }

        articleRepository.save(article);

        ResultActions resultActions = mvc.perform(delete("/api/article/{articleId}", article.getId())
                        .header("Authorization", "Bearer " + accessToken))
                .andDo(print())
                .andExpect(status().isOk())
                ;

        Optional<Article> deleteVerify = articleRepository.findById(article.getId());

        Assertions.assertFalse(deleteVerify.isPresent());
    }
}
