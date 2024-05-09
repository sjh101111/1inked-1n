package com.example.oneinkedoneproject.AllenAiApi;

import com.google.gson.Gson;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

public class AllenAi {
    private static final String AI_API_URL = "https://kdt-api-function.azurewebsites.net/api/v1/question";
    private final HttpClient httpClient;
    private final Gson gson;

    // Default no-argument constructor
    public AllenAi() {
        this.httpClient = HttpClient.newHttpClient();
        this.gson = new Gson();
    }

    // Constructor that accepts external dependencies
    public AllenAi(HttpClient httpClient, Gson gson) {
        this.httpClient = httpClient;
        this.gson = gson;
    }

    // AI로부터 질의 응답을 받아오는 메서드
    public AiResponse queryAi(String clientId, String content) throws IOException {
        AiResponse aiResponse = new AiResponse();
        try {
            // 쿼리 파라미터를 인코딩하여 URL에 추가
            String encodedClientId = URLEncoder.encode(clientId, StandardCharsets.UTF_8);
            String encodedContent = URLEncoder.encode(content, StandardCharsets.UTF_8);
            String queryParams = String.format("?client_id=%s&content=%s", encodedClientId, encodedContent);
            URI uri = URI.create(AI_API_URL + queryParams);

            // HTTP 요청 작성
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(uri)
                    .header("Accept", "application/json")
                    .GET()
                    .build();

            // 요청 전송 및 응답 받기
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            // 응답 상태 코드 확인
            if (response.statusCode() == 200) {
                // 응답을 AiResponse 객체로 변환
                return gson.fromJson(response.body(), AiResponse.class);
            } else {
                System.err.println("AI API 요청 실패, 상태 코드: " + response.statusCode());
            }
        } catch (IOException e) {
            // IOException 자체를 직접 처리
            e.printStackTrace();
            throw new IOException("요청을 처리할 수 없습니다.", e);
        } catch (InterruptedException e) {
            // 특정 예외의 경우 별도로 처리
            throw new RuntimeException("Server logic error", e);
        } catch (Exception e) {
            // 상위 예외를 IOException으로 래핑해서 던짐
            throw new IOException("요청을 처리하는 동안 예기치 않은 오류가 발생했습니다.", e);
        }
        return AiResponse.builder().action(
                        Action.builder().name("error").speak("error").build())
                .content("error").build();
    }

//    public static void main(String[] args) {
//        // AI 서비스 인스턴스 생성
//        AllenAi aiService = new AllenAi();
//
//        // AI API에 요청 보내기
//        AiResponse aiResponse = aiService.queryAi("8c9cad6a-45a6-4129-a938-0db6b017899d", "오늘 날씨 알려줘");
//
//        // 결과 출력
//        if (aiResponse != null) {
//            System.out.println("Name: " + aiResponse.getAction().getName());
//            System.out.println("Speak: " + aiResponse.getAction().getSpeak());
//            System.out.println("Content: " + aiResponse.getContent());
//        } else {
//            System.err.println("AI 응답을 받지 못했습니다.");
//        }
//    }

}




