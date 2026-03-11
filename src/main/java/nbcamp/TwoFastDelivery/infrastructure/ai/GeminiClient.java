package nbcamp.TwoFastDelivery.infrastructure.ai;

import lombok.extern.slf4j.Slf4j;
import nbcamp.TwoFastDelivery.global.exception.CustomException;
import nbcamp.TwoFastDelivery.global.exception.ErrorCode;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.chat.memory.repository.jdbc.JdbcChatMemoryRepository;
import org.springframework.ai.content.Media;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Component;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class GeminiClient {

    private final ChatClient chatClient;
    private final RestTemplate restTemplate = new RestTemplate();

    public GeminiClient(ChatClient.Builder builder, JdbcChatMemoryRepository chatMemoryRepository) {
        // 1. MessageWindowChatMemory를 통해 과거 대화 내역 최대치 설정 (토큰 한도 초과 방지)
        ChatMemory chatMemory = MessageWindowChatMemory.builder()
                .chatMemoryRepository(chatMemoryRepository)
                .maxMessages(20)
                .build();

        // 2. ChatClient 싱글톤에 공통 Advisor(메모리 어드바이저, 로깅 어드바이저) 사전 등록
        this.chatClient = builder
                .defaultAdvisors(
                        MessageChatMemoryAdvisor.builder(chatMemory).build(),
                        new SimpleLoggerAdvisor() // AI 요청/응답 자동 로깅
                )
                .build();
    }

    public String generateDescription(String prompt, String imageUrl, String storeId) {
        // 4. 시스템 프롬프트(주어진 역할 및 제약조건) 분리
        String systemMessage = "당신은 배달 플랫폼 메뉴 설명 전문가이다. " +
                "사용자의 프롬프트와 제공된 이미지를 바탕으로 매력적인 상품 설명을 지어줘.\n" +
                "[제약 조건]\n" +
                "1. 문체: '~함', '~임', '~함께하세요' 등 모든 서술형 어미를 금지.\n" +
                "2. 끝맺음: 반드시 '명사' 또는 '형용사'로 문장을 깔끔하게 끝내. (예: ~의 조화, ~한 맛)\n" +
                "3. 내용: 식재료의 특징, 식감, 맛의 포인트만 압축해서 전달해. 내가 준 사진이 있다면 절대 임의로 생성하지 말고 내가 준 사진을 바탕으로 생성해.\n" +
                "4. 금지: 따옴표, 느낌표, '추천해요' 같은 권유형 표현은 일절 사용하지마.\n" +
                "5. 분량: 답변은 최대한 간결하게 50자 이하로 작성해.";

        try {
            List<Media> mediaList = new ArrayList<>();

            if (imageUrl != null && !imageUrl.isBlank()) {
                try {
                    byte[] imageBytes = restTemplate.getForObject(imageUrl, byte[].class);
                    if (imageBytes != null && imageBytes.length > 0) {
                        String mimeTypeName = "image/jpeg";
                        if (imageUrl.toLowerCase().endsWith(".png"))
                            mimeTypeName = "image/png";
                        else if (imageUrl.toLowerCase().endsWith(".webp"))
                            mimeTypeName = "image/webp";

                        org.springframework.util.MimeType mimeType = MimeTypeUtils.parseMimeType(mimeTypeName);
                        mediaList.add(new Media(mimeType, new ByteArrayResource(imageBytes)));
                    }
                } catch (Exception e) {
                    log.warn("이미지 다운로드 및 변환 실패 (URL: {}), 텍스트만 처리합니다: {}", imageUrl, e.getMessage());
                }
            }

            UserMessage userMessage = UserMessage.builder()
                    .text(prompt) // 시스템 프롬프트를 분리했으므로 순수 요청만 전달
                    .media(mediaList)
                    .build();

            return chatClient.prompt()
                    // 3. 실행 시점에는 해당 대화방의 conversation_id 파라미터만 주입
                    .advisors(spec -> spec.param(ChatMemory.CONVERSATION_ID, storeId))
                    .system(systemMessage)
                    .messages(userMessage)
                    .call()
                    .content();
        } catch (Exception e) {
            log.error("Gemini API 호출 중 오류 발생: {}", e.getMessage(), e);
            throw new CustomException(ErrorCode.INTERNAL_ERROR);
        }
    }
}
