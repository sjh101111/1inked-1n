package com.example.oneinkedoneproject.service;

import com.example.oneinkedoneproject.AllenAiApi.Action;
import com.example.oneinkedoneproject.AllenAiApi.AiResponse;
import com.example.oneinkedoneproject.AllenAiApi.AllenAi;
import com.example.oneinkedoneproject.service.AllenAi.AllenAiService;
import com.example.oneinkedoneproject.service.article.ArticleService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AllenAiServiceTest {
    @InjectMocks
    AllenAiService allenAiService;

    @Mock
    AllenAi allenAi;

    @Test
    void getResponseOfAllenAiTest() throws Exception {
        // Mock a successful response
        AiResponse expectedResponse = AiResponse.builder()
                .action(Action.builder().name("test-action").speak("test-speak").build())
                .content("Test content")
                .build();

        // Simulate the behavior of the AllenAi queryAi method
        doReturn(expectedResponse).when(allenAi).queryAi(any(String.class), any(String.class));

        // Test the service method
        AiResponse actualResponse = allenAiService.getAiResponse("test-client", "test-content");
        assertEquals(expectedResponse, actualResponse);
    }

    @Test
    public void testGetAiResponseIOException() throws IOException {
        // Simulate the behavior of the AllenAi queryAi method to throw an IOException
        when(allenAi.queryAi(anyString(), anyString())).thenThrow(new IOException("Test IO Exception"));

        // Test the service method
        AiResponse actualResponse = allenAiService.getAiResponse("test-client", "test-content");

        // Check that an appropriate error response is returned
        assertEquals("Unable to process request due to an I/O error.", actualResponse.getContent());
    }

}
