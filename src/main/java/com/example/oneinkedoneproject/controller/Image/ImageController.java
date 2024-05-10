package com.example.oneinkedoneproject.controller.Image;

import com.example.oneinkedoneproject.domain.User;
import com.example.oneinkedoneproject.service.image.ImageService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Tag(name = "Image", description = "이미지 API")
public class ImageController {
    private final ImageService imageService;

    @GetMapping("/api/deleteEachImageOfArticle")
    public ResponseEntity<Void> deleteEachImageOfArticle(String articleId, String imageId) {
        imageService.deleteEachImageOfArticle(articleId, imageId);
        return ResponseEntity.ok().build();
    }
}
