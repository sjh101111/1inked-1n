package com.example.oneinkedoneproject.controller.image;

import com.example.oneinkedoneproject.controller.Image.ImageController;
import com.example.oneinkedoneproject.domain.*;
import com.example.oneinkedoneproject.service.image.ImageService;
import com.example.oneinkedoneproject.utils.GenerateIdUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@ExtendWith(MockitoExtension.class)
public class ImageControllerUnitTest {
    @InjectMocks
    ImageController imageController;

    @Mock
    ImageService imageService;

    MockMvc mockMvc;

    private Article article;

    private User user;

    private List<Image> imageList = new ArrayList<>();

    @BeforeEach
    void setUp() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(imageController).build();

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
    void deleteEachImageOfArticleTest() throws Exception {
        //
        String articleId = "article123";
        String imageId = "image123";

        // When
        ResponseEntity<Void> response = imageController.deleteEachImageOfArticle(articleId, imageId);

        // Then
        verify(imageService).deleteEachImageOfArticle(articleId, imageId);
        assertEquals(OK, response.getStatusCode());
    }

}
