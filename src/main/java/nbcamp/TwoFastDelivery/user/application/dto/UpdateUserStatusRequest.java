package nbcamp.TwoFastDelivery.user.application.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import nbcamp.TwoFastDelivery.user.domain.UserStatus;

@Getter
@NoArgsConstructor
public class UpdateUserStatusRequest {

    private UserStatus status;

}
