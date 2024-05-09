package com.example.oneinkedoneproject.service.article;

import com.example.oneinkedoneproject.domain.Article;
import com.example.oneinkedoneproject.domain.Image;
import com.example.oneinkedoneproject.domain.User;
import com.example.oneinkedoneproject.dto.article.AddArticleRequestDto;
import com.example.oneinkedoneproject.dto.article.ArticleResponseDto;
import com.example.oneinkedoneproject.dto.article.UpdateArticleRequestDto;
import com.example.oneinkedoneproject.repository.article.ArticleRepository;
import com.example.oneinkedoneproject.repository.comment.CommentRepository;
import com.example.oneinkedoneproject.repository.image.ImageRepository;
import jakarta.transaction.Transactional;
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

    private final CommentRepository commentRepository;

    @Transactional
    public ArticleResponseDto createArticle(AddArticleRequestDto addArticleRequestDto, User user) {
        List<Image> images = new ArrayList<>();
        String contents = addArticleRequestDto.getContents();

        if (contents == null || contents.trim().isEmpty()) {
            throw new IllegalArgumentException("Article contents cannot be null or empty");
        }

        // 1. Article 생성
        Article article = Article.builder()
                .contents(contents)
                .imageList(images)
                .user(user)
                .build();

        // 2. MultipartFile에서 Image 엔티티 생성 및 Article에 추가
        if (addArticleRequestDto.getFiles() != null && !addArticleRequestDto.getFiles().isEmpty()) {
            try {
                for (MultipartFile file : addArticleRequestDto.getFiles()) {
                    Image image = Image.builder()
                            .img(file.getBytes()).article(article)
                            .build();
                    article.addImage(image); // 편의 메서드로 Article에 Image 추가
                    // 이후 용도로 저장;
                }
            } catch (IOException e) {
                throw new RuntimeException("Failed to process image files", e);
            }
        }

        // 3. 최종 Article을 저장하여 이미지와 연결
        Article savedArticle = articleRepository.save(article);
//        saveArticleImages(images, savedArticle);
        return savedArticle.toDto();
    }

    @Transactional
    public void saveArticleImages(List<Image> images, Article article) {
        if (images != null && !images.isEmpty()) {
            for (Image image : images) {
                image = Image.builder().article(article).img(image.getImg()).build();
                imageRepository.save(image);
            }
        }

        if (images == null) {
            throw new IllegalArgumentException("Images list cannot be null.");
        }
//        imageRepository.save(Image.builder().img(image.getImg()).article(article).build());
    }

    @Transactional
    public List<ArticleResponseDto> readMyAllArticles(User user) {
        String userId = user.getId();
        return articleRepository.findAllByUser_Id(userId).stream()
                .map(article -> article.toDto())
                .toList();
    }

    @Transactional
    public List<ArticleResponseDto> readMainFeedArticles(User user) {
        return articleRepository.findFollowedUserArticlesOrdered(user.getId()).stream()
                .map(article -> article.toDto())
                .toList();
    }

    @Transactional
    public ArticleResponseDto updateArticle(String articleId, UpdateArticleRequestDto updateArticleRequestDto) {
        Article updatedArticle = articleRepository.findById(articleId).orElseThrow(
                () -> new IllegalArgumentException("not found article")
        );

        String contents = updateArticleRequestDto.getContents();

        if (contents == null || contents.trim().isEmpty()) {
            throw new IllegalArgumentException("Article contents cannot be null or empty");
        }

        // 1. Article 생성
        updatedArticle.update(contents);

        List<Image> images = new ArrayList<>();
        if (updateArticleRequestDto.getFiles() != null && !updateArticleRequestDto.getFiles().isEmpty()) {
            try {
                for (MultipartFile file : updateArticleRequestDto.getFiles()) {
                    Image image = Image.builder()
                            .img(file.getBytes()).article(updatedArticle)
                            .build();
                    images.add(image);
                    updatedArticle.addImage(image);
                }
            } catch (IOException e) {
                throw new RuntimeException("Failed to process image files", e);
            }
        }
        return updatedArticle.toDto();
    }

    @Transactional
    public void deleteArticle(String articleId) {
        Article article = articleRepository.findById(articleId).orElseThrow(
                () -> new IllegalArgumentException("id값이 잘못되었습니다")
        );

        commentRepository.deleteByArticle(article);
        imageRepository.deleteByArticleId(article.getId());
        articleRepository.deleteById(articleId);
    }

}
