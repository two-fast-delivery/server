package nbcamp.TwoFastDelivery.auth.presentation;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AuthResponseDto {
    @Getter
    @Builder
    public static class Token {
        private String token;
        private LocalDateTime expired;
    }
}
