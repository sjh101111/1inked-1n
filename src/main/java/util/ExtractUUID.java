package util;

import java.util.UUID;

public class ExtractUUID {

    public static void main(String[] args) {
        // UUID 생성
        UUID uuid = UUID.randomUUID();

        // "-" 제외하고 문자열 추출
        String extractedString = uuid.toString().replace("-", "");

        // 특정 문자만 추출 (예: 숫자만 추출)
        String extractedNumbers = extractedString.replaceAll("[^0-9]", "");

        // 결과 출력
        System.out.println("원본 UUID: " + uuid);
        System.out.println("'-' 제외 문자열: " + extractedString);
        System.out.println("숫자만 추출된 문자열: " + extractedNumbers);
    }

}
