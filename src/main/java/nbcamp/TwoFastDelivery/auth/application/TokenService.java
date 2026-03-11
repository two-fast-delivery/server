package nbcamp.TwoFastDelivery.auth.application;

import java.util.UUID;

public interface TokenService {
    TokenDto create(UUID userId); // 토큰 생성

    void validate(String token);// 토큰의 유효성 검사


    UUID getUserId(String token);// 토큰에서 회원의 식별자 추출
}
