package com.example.oneinkedoneproject.service.externalApi;

import com.example.oneinkedoneproject.dto.externalApi.NaverNewsApiRequest;
import com.example.oneinkedoneproject.dto.externalApi.NaverNewsApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;


import java.nio.charset.StandardCharsets;

@Service
@RequiredArgsConstructor
@Component
public class NaverNewsApiService {
    private final RestTemplate restTemplate;
    @Value("${naver-client}")
    String naverClient;

    @Value("${naver-key}")
    String naverSecret;

    public String apiRequestUrlGenerator(NaverNewsApiRequest request) {
        int urlDisplay= 20; // 한 페이지 기사 20개, 총 100개의 기사, 5페이지
        int urlStart = 1+20*(request.getPage()-1);//
        String urlSort = request.getSort();

        String requestUrl = "https://openapi.naver.com/v1/search/news.json";//요청 URL
        requestUrl = requestUrl
                +"?query="+ request.getQuery()
                +"&display="+ Integer.toString(urlDisplay)
                +"&start="+ Integer.toString(urlStart)
                +"&sort="+urlSort;
        return requestUrl;
    }

    public NaverNewsApiResponse fetchNewsFromNaverAPI(String apiUrl) throws HttpClientErrorException {
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Naver-Client-Id", naverClient);
        headers.set("X-Naver-Client-Secret", naverSecret);
        HttpEntity<String> entity = new HttpEntity<>(headers);


        ResponseEntity<NaverNewsApiResponse> response = restTemplate.exchange(
                apiUrl,
                HttpMethod.GET,
                entity,
                NaverNewsApiResponse.class
        );

        return response.getBody();
    }
}
