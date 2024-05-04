package com.example.oneinkedoneproject.domain;

import com.example.oneinkedoneproject.dto.ArticleResponseDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Table(name = "article")
@Entity
@Getter
@EntityListeners(AuditingEntityListener.class)
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Article {

    @Id
    @Column(name = "article_id", updatable = false, nullable = false)
    private String id;

    @Column(name = "contents", nullable = false)
    private String contents;

    @CreatedDate
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

//    @Column(name = "like_count", columnDefinition = "integer default 0")
//    private int likeCount;
//
//    @Column(name = "reply_count", columnDefinition = "integer default 0")
//    private int replyCount;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "article", cascade = CascadeType.ALL)
    private List<Image> imageList;

    @JsonIgnore
    @Builder.Default
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "article", cascade = CascadeType.ALL)
    private List<Comment> commentList = new ArrayList<>();

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public void update(String contents) {
        this.contents = contents; // 내용은 항상 업데이트
    }

    //기존 게시글에 이미지 수정
    public void addImage(Image image) {
        this.imageList.add(image);
    }

    public ArticleResponseDto toDto() {
        return ArticleResponseDto.builder()
                .contents(contents).createdAt(createdAt)
                .updatedAt(updatedAt).images(imageList)
                .user(user).build();
    }
    
}
