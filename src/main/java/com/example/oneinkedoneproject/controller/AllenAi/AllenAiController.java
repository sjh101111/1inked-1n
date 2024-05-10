package com.example.oneinkedoneproject.controller.AllenAi;

import com.example.oneinkedoneproject.AllenAiApi.Action;
import com.example.oneinkedoneproject.AllenAiApi.AiResponse;
import com.example.oneinkedoneproject.service.AllenAi.AllenAiService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Tag(name = "Allen AI", description = "이력서 첨삭 API")
public class AllenAiController {
    private final AllenAiService allenAiService;

    @GetMapping("/api/resumeReview")
    public ResponseEntity<AiResponse> queryAi(
            @RequestParam("client_id") String clientId,
            @RequestParam("content") String content) {
        AiResponse response = allenAiService.getAiResponse(clientId, content);

        if (response != null) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(AiResponse.builder().action(
                            Action.builder().name("error").speak("error").build())
                            .content("error").build());
        }
    }
}
