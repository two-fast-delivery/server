package nbcamp.TwoFastDelivery.domain.address.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.util.UUID;

@Embeddable
@ToString @Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class AddressId {

    @Column(length = 45, name="id")
    private UUID id;

    public static AddressId of() {
        return AddressId.of(UUID.randomUUID());
    }

    public static AddressId of(UUID id) {
        return new AddressId(id);
    }
}
