package nbcamp.TwoFastDelivery.user.application.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RoleChangeRequestResponse {

    private String requestId;
    private String userId;
    private String currentRole;
    private String requestedRole;
    private String status;
}