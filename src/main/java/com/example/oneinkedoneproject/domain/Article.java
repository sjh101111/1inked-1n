package com.example.oneinkedoneproject.domain;

import com.example.oneinkedoneproject.dto.article.ArticleResponseDto;
import com.example.oneinkedoneproject.dto.image.ImageResponseDto;
import com.example.oneinkedoneproject.utils.GenerateIdUtils;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
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
@Setter
@EntityListeners(AuditingEntityListener.class)
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Article {

    @Id
    @Builder.Default
    @Column(name = "article_id", updatable = false, nullable = false)
    private String id = GenerateIdUtils.generateArticleId();

    @Column(name = "contents", nullable = false, length = 3000)
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
    @JsonManagedReference
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "article", cascade = CascadeType.ALL)
    private List<Image> imageList;

    @JsonIgnore
    @Builder.Default
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "article", cascade = CascadeType.ALL)
    private List<Comment> commentList = new ArrayList<>();

    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
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
        return ArticleResponseDto.builder().id(id)
                .contents(contents).createdAt(createdAt)
                .updatedAt(updatedAt).images(imageList.stream().map(
                        x -> x.toDto()
                ).toList())
                .build();
    }
    
}
