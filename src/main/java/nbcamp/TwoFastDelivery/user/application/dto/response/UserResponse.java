package nbcamp.TwoFastDelivery.user.application.dto.response;

import lombok.Builder;
import lombok.Getter;
import nbcamp.TwoFastDelivery.user.domain.user.User;

@Getter
@Builder
public class UserResponse {

    private String email;
    private String nickname;
    private String role;

    public static UserResponse from(User user) {
        return UserResponse.builder()
                .email(user.getEmail())
                .nickname(user.getNickname())
                .role(user.getRole().name())
                .build();
    }
}