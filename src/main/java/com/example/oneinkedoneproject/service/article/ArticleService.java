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

    public ArticleResponseDto createArticle(AddArticleRequestDto addArticleRequestDto, User user) {
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

        if (!images.isEmpty()) {
            Article article = Article.builder()
                    .contents(addArticleRequestDto.getContents())
                    .imageList(images).user(user)
                    .build();
            Article savedArticle = articleRepository.save(article);
            saveArticleImages(images, savedArticle);
            return savedArticle.toDto();
        } else {
            Article article = Article.builder().user(user).contents(addArticleRequestDto.getContents()).build();
            Article savedArticle = articleRepository.save(article);
            return savedArticle.toDto();
        }
    }

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
    }

    public List<ArticleResponseDto> readMyAllArticles(User user) {
        String userId = user.getId();
        return articleRepository.findAllByUser_Id(userId).stream()
                .map(article -> ArticleResponseDto.builder().id(article.getId()).contents(article.getContents())
                        .createdAt(article.getCreatedAt()).updatedAt(article.getUpdatedAt()).comments(article.getCommentList())
                        .images(article.getImageList()).user(article.getUser()).build())
                .toList();
    }

    public List<ArticleResponseDto> readMainFeedArticles(User user) {
        return articleRepository.findFollowedUserArticlesOrdered(user.getId()).stream()
                .map(article -> ArticleResponseDto.builder().id(article.getId()).contents(article.getContents())
                        .createdAt(article.getCreatedAt()).updatedAt(article.getUpdatedAt()).comments(article.getCommentList())
                        .images(article.getImageList()).user(article.getUser()).build())
                .toList();
    }

    @Transactional
    public ArticleResponseDto updateArticle(String articleId, UpdateArticleRequestDto updateArticleRequestDto) {
        Article updatedArticle = articleRepository.findById(articleId).orElseThrow(
                () -> new IllegalArgumentException("not found article")
        );

        List<Image> images = new ArrayList<>();
        if (updateArticleRequestDto.getFiles() != null && !updateArticleRequestDto.getFiles().isEmpty()) {
            try {
                for (MultipartFile file : updateArticleRequestDto.getFiles()) {
                    Image image = Image.builder()
                            .img(file.getBytes())
                            .build();
                    images.add(image);
                    updatedArticle.addImage(image);
                }
            } catch (IOException e) {
                throw new RuntimeException("Failed to process image files", e);
            }
        }

        updatedArticle.update(updateArticleRequestDto.getContents());

        //이미지 수정
        if (!images.isEmpty()) {
            saveArticleImages(images, updatedArticle);
        }
        return updatedArticle.toDto();
    }

    @Transactional
    public void deleteArticle(String articleId) {
        Article article = articleRepository.findById(articleId).orElseThrow(
                () -> new IllegalArgumentException("id값이 잘못되었습니다")
        );

        commentRepository.deleteByArticle(article);
        imageRepository.deleteByArticle(article);
        articleRepository.deleteById(articleId);
    }

}
