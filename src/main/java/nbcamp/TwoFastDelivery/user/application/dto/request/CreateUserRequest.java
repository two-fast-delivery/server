package nbcamp.TwoFastDelivery.user.application.dto.request;

import lombok.Getter;

@Getter
public class CreateUserRequest {

    private String email;
    private String password;
    private String nickname;
    private String username;
}
