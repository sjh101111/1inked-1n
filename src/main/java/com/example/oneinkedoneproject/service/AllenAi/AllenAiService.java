package com.example.oneinkedoneproject.service.AllenAi;

import com.example.oneinkedoneproject.AllenAiApi.Action;
import com.example.oneinkedoneproject.AllenAiApi.AiResponse;
import com.example.oneinkedoneproject.AllenAiApi.AllenAi;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@Slf4j
@RequiredArgsConstructor
public class AllenAiService {
    private final AllenAi allenAi;

    public AiResponse getAiResponse(String clientId, String content) {
        try {
            return allenAi.queryAi(clientId, content);
        } catch (IOException e) {
            return AiResponse.builder()
                    .action(Action.builder().name("error").speak("error").build())
                    .content("Unable to process request due to an I/O error.")
                    .build();
        }
    }
}

