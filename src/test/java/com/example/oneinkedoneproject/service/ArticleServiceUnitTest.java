package com.example.oneinkedoneproject.service;


import com.example.oneinkedoneproject.domain.Article;
import com.example.oneinkedoneproject.domain.Image;
import com.example.oneinkedoneproject.dto.AddArticleRequestDto;
import com.example.oneinkedoneproject.dto.ArticleResponseDto;
import com.example.oneinkedoneproject.repository.article.ArticleRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
public class ArticleServiceUnitTest {
    @Mock
    ArticleRepository articleRepository;

    @InjectMocks
    ArticleService articleService;

    @Test
    void createArticleTest() throws Exception{
        //given
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
        Mockito.doReturn(responseDto).when(articleRepository.save(any(Article.class)));

        //when
        ArticleResponseDto returnedDto = articleService.createArticle(addArticleRequest);

        //then
        assertThat(returnedDto.getContents().equals(addArticleRequest.getContents()));
        assertThat(returnedDto.getImages().size()).equals(addArticleRequest.getFiles().size());
    }

}
