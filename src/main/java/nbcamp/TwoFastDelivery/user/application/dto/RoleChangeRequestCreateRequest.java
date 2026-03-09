package nbcamp.TwoFastDelivery.user.application.dto;

import lombok.Getter;
import nbcamp.TwoFastDelivery.user.domain.UserRole;

@Getter
public class RoleChangeRequestCreateRequest {

    private UserRole requestedRole;
    private String reason;
}
