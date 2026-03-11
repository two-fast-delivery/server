package nbcamp.TwoFastDelivery.user.application.dto.request;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class CreateUserRequest {

    private String email;
    private String password;
    private String nickname;
    private String username;
}
