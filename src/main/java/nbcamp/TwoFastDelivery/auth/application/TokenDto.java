package nbcamp.TwoFastDelivery.auth.application;

import java.time.LocalDateTime;

public record TokenDto(
        String token, // 발급받은 토큰
        LocalDateTime expired // 토큰 만료 시간
) {}
