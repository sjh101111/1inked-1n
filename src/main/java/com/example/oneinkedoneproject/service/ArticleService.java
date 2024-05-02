package com.example.oneinkedoneproject.service;

import com.example.oneinkedoneproject.domain.Article;
import com.example.oneinkedoneproject.domain.Image;
import com.example.oneinkedoneproject.dto.AddArticleRequestDto;
import com.example.oneinkedoneproject.dto.ArticleResponseDto;
import com.example.oneinkedoneproject.repository.article.ArticleRepository;
import com.example.oneinkedoneproject.repository.image.ImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class ArticleService {
    private final ArticleRepository articleRepository;

    private final ImageRepository imageRepository;

    public ArticleResponseDto createArticle(AddArticleRequestDto addArticleRequestDto) {
        List<Image> images = new ArrayList<>();
        if (addArticleRequestDto.getFiles() != null && !addArticleRequestDto.getFiles().isEmpty()) {
            try {
                for (MultipartFile file : addArticleRequestDto.getFiles()) {
                    Image image = Image.builder()
                            .img(file.getBytes())
                            .build();
                    images.add(image);
                }
            } catch (IOException e) {
                throw new RuntimeException("Failed to process image files", e);
            }
        }

        Article article = Article.builder()
                .contents(addArticleRequestDto.getContents())
                .imageList(images)
                .build();

        saveArticleImages(images,article);

        return articleRepository.save(article).toDto();
    }

    public void saveArticleImages(List<Image> images, Article article) {
        if (images != null && !images.isEmpty()) {
            for (Image image : images) {
                image.setArticle(article);
                imageRepository.save(image);
            }
        }
    }
}
