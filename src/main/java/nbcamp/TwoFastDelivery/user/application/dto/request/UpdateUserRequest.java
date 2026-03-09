package nbcamp.TwoFastDelivery.user.application.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UpdateUserRequest {

    private String nickname;
    private String password;
}
