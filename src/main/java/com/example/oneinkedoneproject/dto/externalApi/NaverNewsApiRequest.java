package com.example.oneinkedoneproject.dto.externalApi;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class NaverNewsApiRequest {
    String query;
    int page;
    String sort;
}
