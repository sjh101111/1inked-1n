package com.example.oneinkedoneproject.repository.comment;

import com.example.oneinkedoneproject.domain.Article;
import com.example.oneinkedoneproject.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, String> {
    void deleteByArticle(Article article);

    List<Comment> findAllByParent(Comment parent);
}
