package com.example.oneinkedoneproject.domain;

import com.example.oneinkedoneproject.dto.ArticleResponseDto;
import com.example.oneinkedoneproject.utils.GenerateIdUtils;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

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
    @Column(name = "article_id", updatable = false, nullable = false)
    private String id;

    @Column(name = "contents", nullable = false)
    private String contents;

    @CreatedDate
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @Column(name = "like_count", columnDefinition = "integer default 0")
    private int likeCount;

    @Column(name = "reply_count", columnDefinition = "integer default 0")
    private int replyCount;

    @OneToMany(mappedBy = "article")
    private List<Image> imageList;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private User user;

    public void update(String contents) {
        this.contents = contents;
    }

    public ArticleResponseDto toDto() {
        return ArticleResponseDto.builder()
                .contents(contents).createdAt(createdAt)
                .updatedAt(updatedAt).images(imageList)
                .user(user).build();
    }
}
