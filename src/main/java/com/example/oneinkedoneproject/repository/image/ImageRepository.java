package com.example.oneinkedoneproject.repository.image;

import com.example.oneinkedoneproject.domain.Article;
import com.example.oneinkedoneproject.domain.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ImageRepository extends JpaRepository<Image, String> {
        void deleteByArticle(Article article);

        void deleteByArticleIdAndId(String articleId, String imageId);
}
