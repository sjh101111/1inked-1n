package com.example.oneinkedoneproject.repository.article;

import com.example.oneinkedoneproject.domain.Article;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ArticleRepository extends JpaRepository<Article, String> {

}
