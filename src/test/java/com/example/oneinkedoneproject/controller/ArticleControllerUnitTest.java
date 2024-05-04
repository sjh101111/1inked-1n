package com.example.oneinkedoneproject.controller;

import com.example.oneinkedoneproject.domain.*;
import com.example.oneinkedoneproject.dto.AddArticleRequestDto;
import com.example.oneinkedoneproject.dto.ArticleResponseDto;
import com.example.oneinkedoneproject.dto.UpdateArticleRequestDto;
import com.example.oneinkedoneproject.service.article.ArticleService;
import com.example.oneinkedoneproject.utils.GenerateIdUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.multipart.MultipartFile;


import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class ArticleControllerUnitTest {
    @InjectMocks
    ArticleController articleController;

    @Mock
    ArticleService articleService;

    MockMvc mockMvc;

    private User user;

    private Article article;

    private List<Image> images;

    MockMultipartFile file = new MockMultipartFile("file", "filename.txt", "text/plain", "some content".getBytes());
    List<MultipartFile> files = List.of(file);

    @BeforeEach
    void setUp() throws Exception {
        this.mockMvc = MockMvcBuilders.standaloneSetup(articleController).build();
        user = User.builder()
                .id(GenerateIdUtils.generateUserId())
                .realname("test")
                .email("test@test.com")
                .password("test")
                .withdraw(false)
                .passwordQuestion(PasswordQuestion.builder().question("a").build())
                .grade(Grade.ROLE_BASIC)
                .build();
//        Authentication auth = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
//        SecurityContextHolder.getContext().setAuthentication(auth);

        images = new ArrayList<>();
        for (MultipartFile fileImage : files) {
            Image image = Image.builder()
                    .img(fileImage.getBytes())
                    .build();
            images.add(image);
        }

        article = Article.builder()
                .id(GenerateIdUtils.generateArticleId())
                .contents("test content")
                .user(user)
                .updatedAt(null)
                .createdAt(null)
                .imageList(images)
                .build();
    }

    @Test
    public void createArticleTest() throws Exception {
        ArticleResponseDto responseDto = ArticleResponseDto.builder()
                .contents("contents")
                .images(images)
                .build();
        Mockito.doReturn(responseDto)
                .when(articleService).createArticle(any(AddArticleRequestDto.class), any(User.class));

        ResultActions resultActions = mockMvc.perform(multipart("/api/article")
                        .file(file)
                        .param("contents", "contents")).andExpect(status().isCreated())
                .andExpect(jsonPath("contents").value("contents"))
                .andExpect(jsonPath("$.images.length()").value(images.size()))
                .andDo(result -> Mockito.verify(articleService).createArticle(any(AddArticleRequestDto.class), any(User.class)));
    }

    @Test
    void readAllMyArticlesTest() throws Exception {
        //given static
        List<Article> articles = new ArrayList<>();
        articles.add(article);
        List<ArticleResponseDto> responseDtos = articles.stream()
                .map(article -> ArticleResponseDto.builder().id(article.getId()).contents(article.getContents())
                        .createdAt(article.getCreatedAt()).updatedAt(article.getUpdatedAt())
                        .images(article.getImageList()).user(article.getUser()).build())
                .toList();
        Mockito.doReturn(responseDtos).when(articleService).readMyAllArticles(any(User.class));

        //when
        ResultActions resultActions = mockMvc.perform(get("/api/myAllArticles"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(article.getId()))
                .andExpect(jsonPath("$[0].contents").value("test content"))
                .andExpect(jsonPath("$[0].images.size()").value(images.size()))
                .andExpect(jsonPath("$[0].comments").doesNotExist())
                .andDo(result -> Mockito.verify(articleService).readMyAllArticles(any(User.class)));
    }

    @Test
    void readMainFeedArticlesTest() throws Exception {
        User followUser =  User.builder()
                .id(GenerateIdUtils.generateUserId())
                .realname("follow test")
                .email("follow test@test.com")
                .password("test")
                .withdraw(false)
                .passwordQuestion(PasswordQuestion.builder().question("a").build())
                .grade(Grade.ROLE_BASIC)
                .build();

        Follow follow = new Follow(GenerateIdUtils.generateArticleId(), followUser, user);

        ArticleResponseDto followArticle = Article.builder()
                .id(GenerateIdUtils.generateArticleId())
                .contents("follow test content")
                .user(followUser)
                .updatedAt(null)
                .createdAt(null)
                .imageList(images)
                .build().toDto();

        List<ArticleResponseDto> followArticles = new ArrayList<>();
        followArticles.add(followArticle);
        Mockito.doReturn(followArticles).when(articleService).readMainFeedArticles(any(User.class));

        ResultActions resultActions = mockMvc.perform(get("/api/mainFeedArticles"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].contents").value(followArticle.getContents()))
                .andExpect(jsonPath("$[0].user.id").value(followUser.getId()))
                .andDo(result -> Mockito.verify(articleService).readMainFeedArticles(any(User.class)));
    }

    @Test
    void updateArticleTest() throws Exception {
        //given
        MockMultipartFile file = new MockMultipartFile("file", "filename.txt", "text/plain", "some content".getBytes());
        MockMultipartFile file1 = new MockMultipartFile("file", "filename.txt", "text/plain", "some content".getBytes());
        List<MultipartFile> updatedfiles = new ArrayList<>();
        updatedfiles.add(file);
        updatedfiles.add(file1);
        List<Image> updatedimages = new ArrayList<>();
        for (MultipartFile fileImage : updatedfiles) {
            Image image = Image.builder()
                    .img(fileImage.getBytes())
                    .build();
            updatedimages.add(image);
        }

        UpdateArticleRequestDto updateArticleRequestDto = UpdateArticleRequestDto.builder()
                .contents("updated content").files(updatedfiles).build();

        ArticleResponseDto articleResponseDto = ArticleResponseDto.builder().id(article.getId()).contents(updateArticleRequestDto.getContents())
                .createdAt(article.getCreatedAt()).updatedAt(article.getUpdatedAt())
                .images(updatedimages).user(article.getUser()).build();

        Mockito.doReturn(articleResponseDto).when(articleService).updateArticle(any(String.class), any(UpdateArticleRequestDto.class));

        //when
        ResultActions resultActions = mockMvc.perform(put("/api/article/{articleId}", article.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.contents").value(updateArticleRequestDto.getContents()))
                .andExpect(jsonPath("$.images.size()").value(updateArticleRequestDto.getFiles().size()))
                .andDo(result -> Mockito.verify(articleService).updateArticle(any(String.class), any(UpdateArticleRequestDto.class)));
    }

    @Test
    void deleteArticle() throws Exception {
        //given

        //when
        ResultActions resultActions = mockMvc.perform(delete("/api/article/{articleId}", article.getId()))
                .andExpect(status().isOk());

        //then
        resultActions.andExpect(jsonPath("$.contents").doesNotExist());
        Mockito.verify(articleService).deleteArticle(any(String.class));
    }

}
