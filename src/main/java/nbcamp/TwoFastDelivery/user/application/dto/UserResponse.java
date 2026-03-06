package nbcamp.TwoFastDelivery.user.application.dto;

import lombok.Builder;
import lombok.Getter;
import nbcamp.TwoFastDelivery.user.domain.User;

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