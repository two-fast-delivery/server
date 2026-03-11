package nbcamp.TwoFastDelivery.auth.presentation;

import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AuthRequestDto {
    @Data
    public static class Token {
        @NotBlank(message="username은 필수 입력값 입니다.")
        private String username;

        @NotBlank(message="password는 필수 입력값 입니다.")
        private String password;
    }
}
