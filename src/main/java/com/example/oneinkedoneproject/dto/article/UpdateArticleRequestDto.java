package com.example.oneinkedoneproject.dto.article;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateArticleRequestDto {
    private String contents;
    private List<MultipartFile> files;
}
