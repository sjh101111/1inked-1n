package com.example.oneinkedoneproject.service.naverNewsApiService;

import com.example.oneinkedoneproject.dto.externalApi.NaverNewsApiRequest;
import com.example.oneinkedoneproject.dto.externalApi.NaverNewsApiResponse;
import com.example.oneinkedoneproject.dto.externalApi.NaverNewsItemDto;
import com.example.oneinkedoneproject.service.externalApi.NaverNewsApiService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
public class NaverNewsApiServiceUnitTest {
    @InjectMocks
    NaverNewsApiService naverNewsApiService;

    @Mock
    RestTemplate restTemplate;

    @Test
    void apiRequestUrlGeneratorTest() {
        NaverNewsApiRequest naverNewsApiRequest = new NaverNewsApiRequest("1",1,"sim");

        String url = naverNewsApiService.apiRequestUrlGenerator(naverNewsApiRequest);

        assertThat(url).isEqualTo("https://openapi.naver.com/v1/search/news.json?query=1" +
                "&display=20&start=1&sort=sim");
    }

    @Test
    void fetchNewsFromNaverApiTest() {
        NaverNewsItemDto dto = new NaverNewsItemDto("1","1","1","1","1");

        NaverNewsApiResponse naverNewsApiResponse = new NaverNewsApiResponse(
                "1",1L,1,1, List.of(dto));

        ResponseEntity<NaverNewsApiResponse> response =new ResponseEntity<>(naverNewsApiResponse, HttpStatus.OK);

        doReturn(response).when(restTemplate).exchange(any(String.class), any(HttpMethod.class), any(HttpEntity.class), eq(NaverNewsApiResponse.class));
        NaverNewsApiResponse newsApiResponse= naverNewsApiService.fetchNewsFromNaverAPI("Test");
        assertThat(newsApiResponse.getItems().get(0).getTitle()).isEqualTo("1");
    }
}
