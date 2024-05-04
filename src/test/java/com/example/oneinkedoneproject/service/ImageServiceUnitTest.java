package com.example.oneinkedoneproject.service;

import com.example.oneinkedoneproject.domain.*;
import com.example.oneinkedoneproject.repository.image.ImageRepository;
import com.example.oneinkedoneproject.service.image.ImageService;
import com.example.oneinkedoneproject.utils.GenerateIdUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class ImageServiceUnitTest {
    @InjectMocks
    ImageService imageService;

    @Mock
    ImageRepository imageRepository;

    private Article article;

    private User user;

    private List<Image> imageList = new ArrayList<>();

    @BeforeEach
    void setUp() {
        byte[] randomData = new byte[1024]; // 1024 바이트 크기의 배열
        new Random().nextBytes(randomData);

        user = User.builder()
                .id(GenerateIdUtils.generateUserId())
                .realname("test")
                .email("test@test.com")
                .password("test")
                .withdraw(false)
                .passwordQuestion(PasswordQuestion.builder().question("a").build())
                .grade(Grade.ROLE_BASIC)
                .build();

        Image image = Image.builder().id(GenerateIdUtils.generateArticleId()).article(article).img(randomData).build();
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
    void deleteEachImageOfArticleTest() {
        //given
        Mockito.doAnswer(invocation -> {
            String imageId = invocation.getArgument(1);
            imageList.removeIf(img -> img.getId().equals(imageId));
            return null;
        }).when(imageRepository).deleteByArticleIdAndImageId(any(String.class), any(String.class));
        //when
        imageService.deleteEachImageOfArticle(article.getId(), imageList.get(0).getId());

        //then
        verify(imageRepository).deleteByArticleIdAndImageId(any(String.class), any(String.class));
        assertThat(imageList).isEmpty();
    }
}
