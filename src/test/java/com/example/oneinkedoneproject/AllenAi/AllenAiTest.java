package com.example.oneinkedoneproject.AllenAi;


import com.example.oneinkedoneproject.AllenAiApi.AiResponse;
import com.example.oneinkedoneproject.AllenAiApi.AllenAi;
import com.google.gson.Gson;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

public class AllenAiTest {
//    @InjectMocks
    private AllenAi allenAi;

    private Gson gson;

    @Mock
    private HttpClient mockHttpClient;

    @Mock
    private HttpResponse<String> mockResponse;

    @BeforeEach
    public void setup() {
// Initialize mocks
        MockitoAnnotations.openMocks(this);

        // Create an instance of AllenAi with the mocked HttpClient
        allenAi = new AllenAi(mockHttpClient, new Gson());


    }

    @Test
    public void testQueryAiSuccess() throws Exception {
        // Mock a successful response
        String jsonResponse = "{ \"action\": { \"name\": \"test-action\", \"speak\": \"test-speak\" }, \"content\": \"Test content\" }";
        doReturn(200).when(mockResponse).statusCode();
//        when(mockResponse.statusCode()).thenReturn(200);
        when(mockResponse.body()).thenReturn(jsonResponse);

        // Mock the behavior of the HttpClient
        when(mockHttpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class))).thenReturn(mockResponse);

        // Invoke the queryAi method and check the result
        AiResponse response = allenAi.queryAi("test-client", "test-content");
        assertEquals("Test content", response.getContent());
        assertEquals("test-action", response.getAction().getName());
    }

    @Test
    public void testQueryAiHttpError() throws Exception {
        // Mock an error response
        when(mockResponse.statusCode()).thenReturn(400);

        // Mock the behavior of the HttpClient
        when(mockHttpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class))).thenReturn(mockResponse);

        // Invoke the queryAi method and expect a default error response
        AiResponse response = allenAi.queryAi("test-client", "test-content");
        assertEquals("error", response.getContent());
    }

    @Test
    public void testQueryAiIOException() {
        // Mock an IOException thrown by the HttpClient
        try {
            when(mockHttpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class)))
                    .thenThrow(new IOException("Test IOException"));

            // Test that the queryAi method throws an IOException
            assertThrows(IOException.class, () -> allenAi.queryAi("test-client", "test-content"));
        } catch (IOException | InterruptedException ignored) {
        }
    }
}
