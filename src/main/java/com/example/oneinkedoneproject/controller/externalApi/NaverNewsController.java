package com.example.oneinkedoneproject.controller.externalApi;

import com.example.oneinkedoneproject.dto.externalApi.NaverNewsApiRequest;
import com.example.oneinkedoneproject.dto.externalApi.NaverNewsApiResponse;
import com.example.oneinkedoneproject.dto.externalApi.NaverNewsItemDto;
import com.example.oneinkedoneproject.service.externalApi.NaverNewsApiService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Tag(name = "Naver new", description = "네이버 뉴스 요청받는 API")
public class NaverNewsController {
    private final NaverNewsApiService naverNewsApiService;

    @GetMapping("/naver-news")
    public ResponseEntity<List<NaverNewsItemDto>> getNewsItems(@ModelAttribute NaverNewsApiRequest request) throws BadRequestException {
        String requestUrl = naverNewsApiService.apiRequestUrlGenerator(request);
        try {
            NaverNewsApiResponse response = naverNewsApiService.fetchNewsFromNaverAPI(requestUrl);
            ResponseEntity<List<NaverNewsItemDto>> responseToFrontend;

            responseToFrontend = ResponseEntity
                    .status(HttpServletResponse.SC_OK)
                    .body(response.getItems());
            return responseToFrontend;
        } catch (HttpClientErrorException e) {
            throw new BadRequestException("부적절한 인자가 전달되었습니다.");
        }
    }

}
