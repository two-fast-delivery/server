package nbcamp.TwoFastDelivery.user.application.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import nbcamp.TwoFastDelivery.user.domain.user.UserStatus;

@Getter
@NoArgsConstructor
public class UpdateUserStatusRequest {

    private UserStatus status;

}
