package com.example.oneinkedoneproject.dto.article;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class AddArticleRequestDto {
    private String contents;
    private List<MultipartFile> files;
}
