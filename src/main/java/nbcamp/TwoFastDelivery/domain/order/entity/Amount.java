package nbcamp.TwoFastDelivery.domain.order.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Amount {
    private Integer value;

    public Amount(Integer value) {
        this.value = value;
    }
}
