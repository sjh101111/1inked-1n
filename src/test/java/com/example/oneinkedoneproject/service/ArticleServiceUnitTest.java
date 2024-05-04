package com.example.oneinkedoneproject.service;


import com.example.oneinkedoneproject.domain.*;
import com.example.oneinkedoneproject.dto.article.AddArticleRequestDto;
import com.example.oneinkedoneproject.dto.article.ArticleResponseDto;
import com.example.oneinkedoneproject.dto.article.UpdateArticleRequestDto;
import com.example.oneinkedoneproject.repository.article.ArticleRepository;
import com.example.oneinkedoneproject.repository.comment.CommentRepository;
import com.example.oneinkedoneproject.repository.follow.FollowRepository;
import com.example.oneinkedoneproject.repository.image.ImageRepository;
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
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ArticleServiceUnitTest {
    @Mock
    ArticleRepository articleRepository;

    @InjectMocks
    ArticleService articleService;

    @Mock
    ImageRepository imageRepository;

    @Mock
    CommentRepository commentRepository;

    @Mock
    FollowRepository followRepository;

    private User user;

    private Article article;

    private List<Image> imageList = new ArrayList<>();

    private Follow follow;

    MockMultipartFile file = new MockMultipartFile("file", "filename.txt", "text/plain", "some content".getBytes());
    List<MultipartFile> files = List.of(file);


    @BeforeEach
    void setUp() {
        user = User.builder()
                .id(GenerateIdUtils.generateUserId())
                .realname("test")
                .email("test@test.com")
                .password("test")
                .withdraw(false)
                .build();

        byte[] randomData = new byte[1024]; // 1024 바이트 크기의 배열
        new Random().nextBytes(randomData);

        Image image = Image.builder().article(article).img(randomData).build();
        imageList.add(image);

        article = Article.builder()
                .id(GenerateIdUtils.generateArticleId())
                .contents("test content")
                .user(user)
                .updatedAt(null)
                .createdAt(null)
                .imageList(imageList)
                .build();
    }

    @Test
    void createArticleTest() throws Exception {
        //given
        List<Image> images = new ArrayList<>();
        for (MultipartFile fileImage : files) {
            Image image = Image.builder()
                    .img(fileImage.getBytes())
                    .build();
            images.add(image);
        }

        AddArticleRequestDto addArticleRequest = new AddArticleRequestDto(
                "create content", files);

        if (!images.isEmpty()) {
            for (Image image : images) {
                image.setArticle(article);
            }
        }

        Mockito.doReturn(Article.builder().contents(addArticleRequest.getContents()).imageList(images).build())
                .when(articleRepository).save(any(Article.class));

        //when
        ArticleResponseDto returnedDto = articleService.createArticle(addArticleRequest, user);

        //then
        Mockito.verify(articleRepository).save(any(Article.class));
        assertThat(returnedDto.getContents().equals(addArticleRequest.getContents()));
        assertThat(returnedDto.getImages().size()).isEqualTo(addArticleRequest.getFiles().size());
        assertThat(returnedDto.getImages().get(0).getArticle()).isNotNull();
    }

    @Test
    void createArticleWithEmptyFilesTest() {
        //given
        List<Image> imageList = new ArrayList<>();
        AddArticleRequestDto addArticleRequestDto = AddArticleRequestDto.builder()
                .files(new ArrayList<>()).contents("contentsWithEmptyFiles").build();
        Mockito.doReturn(Article.builder().imageList(imageList).contents(addArticleRequestDto.getContents())
                .build()).when(articleRepository).save(any(Article.class));

        ArticleResponseDto articleResponseDto = articleService.createArticle(addArticleRequestDto, user);

        //when
        assertThat(articleResponseDto.getContents()).isEqualTo(addArticleRequestDto.getContents());
        assertThat(articleResponseDto.getImages().size()).isEqualTo(0);
    }

    @Test
    void createArticleWithNullFilesTest() {
        AddArticleRequestDto addArticleRequestDto = AddArticleRequestDto.builder()
                .contents("contentsWithNullFiles").build();
        Mockito.doReturn(Article.builder().contents(addArticleRequestDto.getContents())
                .build()).when(articleRepository).save(any(Article.class));

        ArticleResponseDto articleResponseDto = articleService.createArticle(addArticleRequestDto, user);

        //when
        assertThat(articleResponseDto.getContents()).isEqualTo(addArticleRequestDto.getContents());
        assertThat(articleResponseDto.getImages()).isNull();
    }

    @Test
    void createArticleWithIOExceptionTest() {
        // Given
        MultipartFile mockFile = mock(MultipartFile.class);

        try {
            Mockito.when(mockFile.getBytes()).thenThrow(new IOException("Test exception"));
        } catch (IOException e) {
            // 이 블록은 실제 테스트 실행 중에는 접근되지 않음
        }

        List<MultipartFile> testfiles = new ArrayList<>();
        testfiles.add(mockFile);

        AddArticleRequestDto requestDto = AddArticleRequestDto.builder()
                .contents("Test Content")
                .files(testfiles)
                .build();

        assertThrows(RuntimeException.class, () ->
                {
                    articleService.createArticle(requestDto, user);
                },
                "Failed to process image files");
    }

    @Test
    void readMainFeedArticles() {
        //given
        User toUser = User.builder()
                .id(GenerateIdUtils.generateUserId())
                .realname("test")
                .email("test@test.com")
                .password("test")
                .withdraw(false)
                .build();

        Article toUserArticle = Article.builder()
                .id(GenerateIdUtils.generateArticleId())
                .contents("toUser test content")
                .user(toUser)
                .updatedAt(null)
                .createdAt(null)
                .imageList(imageList)
                .build();
        follow = new Follow(GenerateIdUtils.generateUserId(),
                toUser , user );

        List<Article> mainFeedTestArticles = new ArrayList<>();
        mainFeedTestArticles.add(toUserArticle);

        Mockito.doReturn(mainFeedTestArticles).when(articleRepository).findFollowedUserArticlesOrdered(user.getId());

        //when
        articleService.readMainFeedArticles(user);

        //then
        assertThat(mainFeedTestArticles.get(0).getContents()).isEqualTo(toUserArticle.getContents());
    }

    @Test
    void testSaveArticleImages_withImages() {
        // 시나리오 1: 이미지 리스트가 비어있지 않은 경우
        //given 필드값
        byte[] randomData = new byte[1024]; // 1024 바이트 크기의 배열
        new Random().nextBytes(randomData);

        Image image = Image.builder().article(article).img(randomData).build();
        imageList.add(image);

        //when
        articleService.saveArticleImages(imageList, article);

        //then
        Mockito.verify(imageRepository, times(2)).save(any(Image.class)); // save 호출 확인
    }

    @Test
    void testSaveArticleImages_withEmptyList() {
        // 시나리오 2: 이미지 리스트가 비어 있는 경우
        // 적절한 Article 객체 생성
        List<Image> images = new ArrayList<>();

        Article testArticle = Article.builder()
                .id(GenerateIdUtils.generateArticleId())
                .contents("test content")
                .user(user)
                .updatedAt(null)
                .createdAt(null)
                .imageList(images)
                .build();

        articleService.saveArticleImages(images, testArticle);

        Mockito.verify(imageRepository, never()).save(any(Image.class)); // save 호출되지 않음 확인
    }

    @Test
    void testSaveArticleImages_withNull() {
        // 시나리오 3: 이미지 리스트가 null인 경우
        Article article = new Article(); // 적절한 Article 객체 생성

        assertThrows(IllegalArgumentException.class, () -> {
            articleService.saveArticleImages(null, article);
        });
    }

    @Test
    void readMyAllArticleTest() throws Exception {
        //given beforeEach 사전 준비된 데이터
        List<Article> articleList = new ArrayList<>();
        articleList.add(article);
        Mockito.doReturn(articleList).when(articleRepository).findAllByUser_Id(user.getId());

        //when
        List<ArticleResponseDto> articleResponseDtos = articleService.readMyAllArticles(user);

        //then
        Mockito.verify(articleRepository).findAllByUser_Id(user.getId());
        assertThat(articleResponseDtos.get(0).getContents()).isEqualTo("test content");
        assertThat(articleResponseDtos.get(0).getImages().size()).isEqualTo(1);
        assertThat(articleResponseDtos.get(0).getUser().getEmail()).isEqualTo("test@test.com");
    }

    @Test
    void updateArticle() throws Exception {
        //given
        MockMultipartFile file = new MockMultipartFile("file", "filename.txt", "text/plain", "some content".getBytes());
        MockMultipartFile file1 = new MockMultipartFile("file", "filename.txt", "text/plain", "some content".getBytes());
        List<MultipartFile> files = new ArrayList<>();
        files.add(file);
        files.add(file1);

        UpdateArticleRequestDto updateArticleRequestDto = UpdateArticleRequestDto.builder()
                .files(files).contents("updated content").build();
        Mockito.doReturn(Optional.of(article)).when(articleRepository).findById(any(String.class));

        //when
        ArticleResponseDto updatedDto = articleService.updateArticle(article.getId(), updateArticleRequestDto);

        //then
        assertThat(updatedDto.getContents()).isEqualTo("updated content");
        assertThat(updatedDto.getImages().size()).isEqualTo(article.getImageList().size());
        verify(articleRepository).findById(article.getId());
        verify(imageRepository, times(2)).save(any(Image.class));  //
    }

    @Test
    public void updateArticle_InvalidArticleId_ThrowsException() {
        String articleId = "invalid_id";
        UpdateArticleRequestDto updateArticleRequestDto = UpdateArticleRequestDto.builder().contents("cc").build();

        when(articleRepository.findById(articleId)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> {
            articleService.updateArticle(articleId, updateArticleRequestDto);
        }); }

    @Test
    public void testUpdateArticle_FailureInFileProcessing() throws IOException {String articleId = "valid_id";
        Article existingArticle = Article.builder().id(GenerateIdUtils.generateArticleId()).build();

        MultipartFile file = mock(MultipartFile.class);
        ArrayList<MultipartFile> files = new ArrayList<>();
        files.add(file);
        UpdateArticleRequestDto updateArticleRequestDto = UpdateArticleRequestDto.builder().files(files).contents("dd").build();

        when(articleRepository.findById(articleId)).thenReturn(Optional.of(existingArticle));
        try {
            when(file.getBytes()).thenThrow(new IOException("Failed to read file"));
        } catch (IOException e) {
            // This block is necessary to handle the checked exception in the mock setup
        }

        assertThrows(RuntimeException.class, () -> {
            articleService.updateArticle(articleId, updateArticleRequestDto);
        });    }

    @Test
    void deleteArticleTest() throws Exception {
        //given
        Comment comment = Comment.builder().comments("test").article(article).user(user).build();
        Mockito.when(articleRepository.findById(any(String.class)))
                .thenReturn(Optional.of(article)); // 첫 호출에는 article을 반환
//                .thenReturn(Optional.empty());    // 이후 호출에는 빈 Optional 반환
// When
        articleService.deleteArticle(article.getId());

        // Then
        // Verify that each repository method is called once
        verify(articleRepository).findById(article.getId());
        verify(commentRepository).deleteByArticle(article);
        verify(imageRepository).deleteByArticle(article);
        verify(articleRepository).deleteById(article.getId());

        // To ensure no further methods are called on the mock
        verifyNoMoreInteractions(articleRepository, commentRepository, imageRepository);
    }

    @Test
    void deleteArticleTestWithIOException() {
        // 잘못된 articleId를 사용하여 deleteArticle 메서드를 호출하면 IllegalArgumentException이 발생해야 합니다.
        String invalidArticleId = "invalid_id";

        // IllegalArgumentException이 발생하는지 확인합니다.
        assertThrows(IllegalArgumentException.class, () -> articleService.deleteArticle(invalidArticleId));
    }
}
