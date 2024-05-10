package com.example.oneinkedoneproject.dto.resume;

import com.example.oneinkedoneproject.domain.Resume;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ResumeResponseDto {
    private String id;
    private String contents;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public ResumeResponseDto(Resume resume) {
        this.id = resume.getId();
        this.contents = resume.getContents();
        this.createdAt = resume.getCreateAt();
        this.updatedAt = resume.getUpdatedAt();
    }
}
