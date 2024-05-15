package com.example.oneinkedoneproject.AllenAiApi;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AiResponse {
    private Action action;
    private String content;
}
