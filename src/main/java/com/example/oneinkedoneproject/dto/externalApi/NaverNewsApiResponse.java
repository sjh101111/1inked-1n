package com.example.oneinkedoneproject.dto.externalApi;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class NaverNewsApiResponse {
    String lastBuildDate;
    Long total;
    int start;
    int display;
    List<NaverNewsItemDto> items;
}