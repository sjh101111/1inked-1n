package com.example.oneinkedoneproject.controller;

import com.example.oneinkedoneproject.domain.Article;
import com.example.oneinkedoneproject.domain.Image;
import com.example.oneinkedoneproject.dto.AddArticleRequestDto;
import com.example.oneinkedoneproject.dto.ArticleResponseDto;
import com.example.oneinkedoneproject.service.ArticleService;
import com.fasterxml.jackson.databind.ObjectMapper;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class ArticleControllerUnitTest {
    @InjectMocks
    ArticleController articleController;

    @Mock
    ArticleService articleService;

    MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(articleController).build();
    }

    @Test
    public void createArticleTest() throws Exception {
        MockMultipartFile file = new MockMultipartFile("file", "filename.txt", "text/plain", "some content".getBytes());
        List<MultipartFile> files = List.of(file);
        List<Image> images = new ArrayList<>();
        for (MultipartFile fileImage : files) {
            Image image = Image.builder()
                    .img(fileImage.getBytes())
                    .build();
            images.add(image);
        }

        AddArticleRequestDto addArticleRequest = new AddArticleRequestDto(
                "contents", files);
        ArticleResponseDto responseDto = ArticleResponseDto.builder()
                .contents("contents")
                .images(images)
                .build();
        Mockito.doReturn(responseDto)
                .when(articleService).createArticle(any(AddArticleRequestDto.class));

        ResultActions resultActions = mockMvc.perform(multipart("/api/article")
                        .file(file)
                        .param("contents", "contents")).andExpect(status().isCreated())
                .andExpect(jsonPath("contents").value("contents"))
                .andExpect(jsonPath("$.images.length()").value(images.size()));
    }


}
