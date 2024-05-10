package com.example.oneinkedoneproject.dto.resume;

import com.example.oneinkedoneproject.domain.Resume;
import com.example.oneinkedoneproject.domain.User;
import com.example.oneinkedoneproject.utils.GenerateIdUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AddResumeRequestDto {
    private String contents;

    public Resume toEntity(User user) {
        return Resume.builder()
                .id(GenerateIdUtils.generateResumeId())
                .user(user)
                .contents(contents)
                .build();
    }
}
