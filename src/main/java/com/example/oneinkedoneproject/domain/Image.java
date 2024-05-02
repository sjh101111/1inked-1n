package com.example.oneinkedoneproject.domain;

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
    @Column(name = "image_id", nullable = false, updatable = false)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "article_id")
    private Article article;

    @Column(name = "img", columnDefinition = "LONGBLOB")
    private byte[] img;

    public void update(byte[] img) {
        this.img = img;
    }
}
