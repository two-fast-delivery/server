package nbcamp.TwoFastDelivery.infrastructure.ai;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nbcamp.TwoFastDelivery.global.exception.CustomException;
import nbcamp.TwoFastDelivery.global.exception.ErrorCode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class GeminiClient {

    @Value("${gemini.api.key}")
    private String apiKey;

    @Value("${gemini.api.url}")
    private String apiUrl;

    private final RestTemplate restTemplate = new RestTemplate();

    public String generateDescription(String prompt, String imageUrl) {
        String fullPrompt = prompt
                + " 배달 플랫폼에 등록할 상품 설명이라는 것을 감안해서 만들어줘. '~해보세요' 이런 식의 답변은 피해주고 상품에 대한 설명만 해줘. 답변을 최대한 간결하게 50자 이하로.";

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            List<Map<String, Object>> parts = new ArrayList<>();

            // 1. 텍스트 프롬프트 파트 추가
            Map<String, Object> textPart = new HashMap<>();
            textPart.put("text", fullPrompt);
            parts.add(textPart);

            // 2. 이미지 URL이 존재하면 다운로드 후 Base64로 넘김
            if (imageUrl != null && !imageUrl.isBlank()) {
                try {
                    byte[] imageBytes = restTemplate.getForObject(imageUrl, byte[].class);
                    if (imageBytes != null && imageBytes.length > 0) {
                        String mimeType = "image/jpeg"; // 기본값
                        if (imageUrl.toLowerCase().endsWith(".png"))
                            mimeType = "image/png";
                        else if (imageUrl.toLowerCase().endsWith(".webp"))
                            mimeType = "image/webp";

                        String base64Image = Base64.getEncoder().encodeToString(imageBytes);

                        Map<String, Object> inlineData = new HashMap<>();
                        inlineData.put("mimeType", mimeType);
                        inlineData.put("data", base64Image);

                        Map<String, Object> imagePart = new HashMap<>();
                        imagePart.put("inlineData", inlineData);

                        parts.add(imagePart);
                        log.info("이미지를 Base64 인코딩하여 프롬프트 파트에 추가했습니다.");
                    }
                } catch (Exception e) {
                    log.warn("이미지 다운로드 및 Base64 변환 실패 (URL: {}), 이미지를 제외하고 텍스트만 요청합니다: {}", imageUrl, e.getMessage());
                }
            }

            Map<String, Object> content = new HashMap<>();
            content.put("parts", parts);

            Map<String, Object> body = new HashMap<>();
            body.put("contents", Collections.singletonList(content));

            String urlWithKey = apiUrl + "?key=" + apiKey;
            HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(body, headers);

            ResponseEntity<Map<String, Object>> responseEntity = restTemplate.exchange(
                    urlWithKey,
                    HttpMethod.POST,
                    requestEntity,
                    new ParameterizedTypeReference<Map<String, Object>>() {
                    });

            return extractTextFromResponse(responseEntity.getBody());
        } catch (CustomException e) {
            throw e;
        } catch (Exception e) {
            log.error("Gemini API 호출 중 오류 발생: {}", e.getMessage(), e);
            throw new CustomException(ErrorCode.INTERNAL_ERROR);
        }
    }

    private String extractTextFromResponse(Map<String, Object> response) {
        // 1. 전체 응답에서 'candidates' 배열이 존재하는지 확인합니다.
        // Gemini API는 응답 구조를 'candidates'라는 배열로 내려주기 때문에 해당 키가 없으면 비정상적인 응답입니다.
        if (response == null || !response.containsKey("candidates")) {
            log.error("Gemini 응답에 candidates 필드가 없습니다.");
            throw new CustomException(ErrorCode.INTERNAL_ERROR);
        }

        try {
            // 2. 'candidates' 필드가 여러 개의 답변 후보를 담는 List(배열) 형식이 맞는지 검증합니다.
            Object candidatesObj = response.get("candidates");
            if (!(candidatesObj instanceof List)) {
                throw new CustomException(ErrorCode.INTERNAL_ERROR);
            }
            List<?> candidates = (List<?>) candidatesObj;

            // 3. 답변 후보 리스트가 비어있는지 확인합니다.
            // 차단(Block) 정책 등에 의해 AI가 아무런 답변을 반환하지 않았을 수 있습니다.
            if (candidates.isEmpty()) {
                throw new CustomException(ErrorCode.INTERNAL_ERROR);
            }

            // 4. 가장 첫 번째(0번째) 답변 후보 객체를 가져오고, 그것이 Map(JSON 객체) 형태인지 검증합니다.
            Object candidateObj = candidates.get(0);
            if (!(candidateObj instanceof Map)) {
                throw new CustomException(ErrorCode.INTERNAL_ERROR);
            }
            Map<?, ?> candidate = (Map<?, ?>) candidateObj;

            // 5. 첫 번째 답변 후보에서 실제 응답 내용이 담긴 'content' 필드를 꺼내고 Map 형태인지 검증합니다.
            Object contentObj = candidate.get("content");
            if (!(contentObj instanceof Map)) {
                throw new CustomException(ErrorCode.INTERNAL_ERROR);
            }
            Map<?, ?> content = (Map<?, ?>) contentObj;

            // 6. 내용은 다시 여러 개의 조각(text 조각, image 조각 등)으로 나뉘는데, 'parts' 필드가 List인지 검증합니다.
            Object partsObj = content.get("parts");
            if (!(partsObj instanceof List)) {
                throw new CustomException(ErrorCode.INTERNAL_ERROR);
            }
            List<?> parts = (List<?>) partsObj;

            // 7. 조각 리스트가 비어있지 않은지 검증합니다. (빈 답변 방지)
            if (parts.isEmpty()) {
                throw new CustomException(ErrorCode.INTERNAL_ERROR);
            }

            // 8. 첫 번째 조각 객체를 가져오고, Map 형태인지 검증합니다.
            Object firstPartObj = parts.get(0);
            if (!(firstPartObj instanceof Map)) {
                throw new CustomException(ErrorCode.INTERNAL_ERROR);
            }
            Map<?, ?> firstPart = (Map<?, ?>) firstPartObj;

            // 9. 최종적으로 첫 번째 조각 안에서 순수한 문자열인 "text"의 값을 추출하여 반환합니다.
            return (String) firstPart.get("text");
        } catch (Exception e) {
            log.error("Gemini 리스폰스 파싱 오류: {}", e.getMessage());
            throw new CustomException(ErrorCode.INTERNAL_ERROR);
        }
    }
}
