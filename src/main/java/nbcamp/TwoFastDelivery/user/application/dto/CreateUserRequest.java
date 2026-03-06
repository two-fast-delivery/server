package nbcamp.TwoFastDelivery.user.application.dto;

import lombok.Getter;

@Getter
public class CreateUserRequest {

    private String email;
    private String password;
    private String nickname;
    private String username;
}
