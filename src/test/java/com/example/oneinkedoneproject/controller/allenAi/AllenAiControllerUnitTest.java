package com.example.oneinkedoneproject.controller.allenAi;

import com.example.oneinkedoneproject.AllenAiApi.Action;
import com.example.oneinkedoneproject.AllenAiApi.AiResponse;
import com.example.oneinkedoneproject.controller.AllenAi.AllenAiController;
import com.example.oneinkedoneproject.service.allenAi.AllenAiService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class AllenAiControllerUnitTest {
    @InjectMocks
    AllenAiController allenAiController;

    @Mock
    AllenAiService allenAiService;

    MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(allenAiController).build();
    }

    @Test
    void getResponseOfAllenAiTest()throws Exception {
        AiResponse expectedResponse = AiResponse.builder()
                .action(Action.builder().name("test-action").speak("test-speak").build())
                .content("Test content")
                .build();
        Mockito.doReturn(expectedResponse).when(allenAiService).getAiResponse(any(String.class), any(String.class));

        ResultActions resultActions = mockMvc.perform(get("/api/resumeReview")
                        .param("client_id", "test-client")
                        .param("content", "test-content"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").value(expectedResponse.getContent()));
    }

    @Test
    void testInternalServerError() throws Exception {
        // Mock the service to return null or an error response
        Mockito.doReturn(null).when(allenAiService).getAiResponse(Mockito.anyString(), Mockito.anyString());

        // Perform the GET request
        ResultActions resultActions = mockMvc.perform(get("/api/resumeReview")
                        .param("client_id", "test-client")
                        .param("content", "test-content"))
                .andExpect(status().isInternalServerError()) // Expecting 500 status code
                .andExpect(jsonPath("$.content").value("error")); // Checking the error content
    }

}
