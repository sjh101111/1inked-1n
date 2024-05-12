package com.example.oneinkedoneproject.controller.externalApi;

import com.example.oneinkedoneproject.dto.externalApi.NaverNewsApiRequest;
import com.example.oneinkedoneproject.dto.externalApi.NaverNewsApiResponse;
import com.example.oneinkedoneproject.dto.externalApi.NaverNewsItemDto;
import com.example.oneinkedoneproject.service.externalApi.NaverNewsApiService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RestController;

import java.io.UnsupportedEncodingException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Tag(name = "Naver new", description = "네이버 뉴스 요청받는 API")
public class NaverNewsController {
    private final NaverNewsApiService naverNewsApiService;

    @GetMapping("/naver-news/blah")
    public ResponseEntity<String> naverAPITest(){
        return ResponseEntity.status(HttpServletResponse.SC_OK).body("okok");
    }
    @GetMapping("/naver-news")
    public ResponseEntity<List<NaverNewsItemDto>> getNewsItems(@ModelAttribute NaverNewsApiRequest request) throws UnsupportedEncodingException {
        String requestUrl = naverNewsApiService.apiRequestUrlGenerator(request);
        NaverNewsApiResponse response = naverNewsApiService.fetchNewsFromNaverAPI(requestUrl);
        ResponseEntity<List<NaverNewsItemDto>> responseToFrontend;
        if (response != null) {
            responseToFrontend = ResponseEntity
                    .status(HttpServletResponse.SC_OK)
                    .body(response.getItems());
            return responseToFrontend;
        } else {
            responseToFrontend = ResponseEntity
                    .status(HttpServletResponse.SC_BAD_REQUEST)
                    .body(null);
            return responseToFrontend;
        }
    }

}
