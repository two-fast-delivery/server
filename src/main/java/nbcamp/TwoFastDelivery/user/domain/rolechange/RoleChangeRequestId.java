package nbcamp.TwoFastDelivery.user.domain.rolechange;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RoleChangeRequestId {

    @Column(name = "id")
    private UUID id;

    private RoleChangeRequestId(UUID id) {
        this.id = id;
    }

    public static RoleChangeRequestId of(UUID id) {
        return new RoleChangeRequestId(id);
    }

    public static RoleChangeRequestId newId() {
        return new RoleChangeRequestId(UUID.randomUUID());
    }
}