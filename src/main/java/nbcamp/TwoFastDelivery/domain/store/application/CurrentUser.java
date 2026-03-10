package nbcamp.TwoFastDelivery.domain.store.application;

import java.util.Set;
import java.util.UUID;

public record CurrentUser(
        UUID id,
        Set<String> roles
) {

    public boolean hasRole(String role) {
        return roles != null && roles.contains(role);
    }
}