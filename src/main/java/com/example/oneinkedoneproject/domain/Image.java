package com.example.oneinkedoneproject.domain;

import com.example.oneinkedoneproject.utils.GenerateIdUtils;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

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

    @Column(name = "img", columnDefinition = "LONGBLOB")
    private byte[] img;

    @ManyToOne
    @JoinColumn(name = "article_id")
    private Article article;

    public void update(byte[] img) {
        this.img = img;
    }
}
