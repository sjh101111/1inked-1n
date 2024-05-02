package com.example.oneinkedoneproject.controller;

import com.example.oneinkedoneproject.dto.AddArticleRequestDto;
import com.example.oneinkedoneproject.dto.ArticleResponseDto;
import com.example.oneinkedoneproject.repository.article.ArticleRepository;
import com.example.oneinkedoneproject.service.ArticleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ArticleController {
    private final ArticleService articleService;

    @PostMapping("/api/article")
    public ResponseEntity<ArticleResponseDto> createArticle(@ModelAttribute AddArticleRequestDto addArticleRequestDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(articleService.createArticle(addArticleRequestDto));
    }

}
