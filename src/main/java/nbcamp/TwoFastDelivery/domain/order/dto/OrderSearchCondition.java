package nbcamp.TwoFastDelivery.domain.order.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class OrderSearchCondition {
    private String keyword;
    private String period;
}
