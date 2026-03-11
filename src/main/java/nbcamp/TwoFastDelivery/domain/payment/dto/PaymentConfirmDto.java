package nbcamp.TwoFastDelivery.domain.payment.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@NoArgsConstructor
public class PaymentConfirmDto {
        private String paymentKey;
        private UUID orderId;
        private Integer totalAmount;
}
