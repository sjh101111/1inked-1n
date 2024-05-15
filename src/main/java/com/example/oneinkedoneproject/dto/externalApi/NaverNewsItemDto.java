package com.example.oneinkedoneproject.dto.externalApi;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class NaverNewsItemDto {
    private String title;
    private String originalLink;
    private String link;
    private String description;
    private String punDate;
}
