package com.example.oneinkedoneproject.repository.article;

import com.example.oneinkedoneproject.domain.Article;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArticleRepository extends JpaRepository<Article, String> {
    List<Article> findAllByUser_Id(String userId);

    @Query("SELECT a FROM Article a WHERE a.user IN (SELECT f.toUser FROM Follow f WHERE f.fromUser.id = :userId) ORDER BY a.createdAt ASC")
    List<Article> findFollowedUserArticlesOrdered(@Param("userId") String userId);

    Article findByUserId(String userId);

}
