package com.example.oneinkedoneproject.dto.image;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ImageResponseDto {
    private String id;
    private byte[] img;
    private String articleId;
}
