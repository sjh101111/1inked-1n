package com.example.oneinkedoneproject.dto.externalApi;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class NaverNewsApiRequest {
    String query;
    int page;
    String sort;
}
