package nbcamp.TwoFastDelivery.user.application.dto.request;

import lombok.Getter;
import nbcamp.TwoFastDelivery.user.domain.user.UserRole;

@Getter
public class RoleChangeRequestCreateRequest {

    private UserRole requestedRole;
    private String reason;
}
