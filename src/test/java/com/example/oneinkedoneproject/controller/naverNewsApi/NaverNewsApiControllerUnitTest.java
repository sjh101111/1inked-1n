package com.example.oneinkedoneproject.controller.naverNewsApi;

import com.example.oneinkedoneproject.controller.externalApi.NaverNewsController;
import com.example.oneinkedoneproject.dto.externalApi.NaverNewsApiRequest;
import com.example.oneinkedoneproject.dto.externalApi.NaverNewsApiResponse;
import com.example.oneinkedoneproject.dto.externalApi.NaverNewsItemDto;
import com.example.oneinkedoneproject.service.externalApi.NaverNewsApiService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class NaverNewsApiControllerUnitTest {
    @InjectMocks
    NaverNewsController naverNewsController;

    @Mock
    NaverNewsApiService naverNewsApiService;

    MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(naverNewsController).build();
    }

    @Test
    void getNewsItemsTest() throws Exception {

        NaverNewsItemDto dto = new NaverNewsItemDto("1","1","1","1","1");

        NaverNewsApiResponse naverNewsApiResponse = new NaverNewsApiResponse(
                "1",1L,1,1, List.of(dto));

        doReturn("url").when(naverNewsApiService).apiRequestUrlGenerator(any(NaverNewsApiRequest.class));
        doReturn(naverNewsApiResponse).when(naverNewsApiService).fetchNewsFromNaverAPI("url");

        ResultActions resultActions = mockMvc.perform(get("/naver-news")
                        .param("query", "경제")
                        .param("page", "5")
                        .param("sort", "date"))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$[0].title").value("1"));
    }
}
