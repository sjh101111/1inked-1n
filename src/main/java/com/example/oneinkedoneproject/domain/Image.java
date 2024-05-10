package com.example.oneinkedoneproject.domain;

import com.example.oneinkedoneproject.dto.image.ImageResponseDto;
import com.example.oneinkedoneproject.utils.GenerateIdUtils;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

@Table(name = "image")
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Image {
    @Id
    @Builder.Default
    @Column(name = "image_id", nullable = false, updatable = false)
    private String id = GenerateIdUtils.generateImageId();

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "article_id", nullable = false)
    private Article article;

    @Column(name = "img", columnDefinition = "LONGBLOB")
    private byte[] img;

    public void update(byte[] img) {
        this.img = img;
    }

    public ImageResponseDto toDto() {
        return ImageResponseDto.builder()
                .id(id).articleId(article.getId()).img(img)
                .build();
    }

}
