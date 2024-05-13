package com.example.oneinkedoneproject.service.image;

import com.example.oneinkedoneproject.domain.Article;
import com.example.oneinkedoneproject.domain.User;
import com.example.oneinkedoneproject.repository.article.ArticleRepository;
import com.example.oneinkedoneproject.repository.image.ImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ImageService {
    private final ImageRepository imageRepository;

    public void deleteEachImageOfArticle(String articleId, String imageId) {
        imageRepository.deleteByArticleIdAndId(articleId, imageId);
    }

}
