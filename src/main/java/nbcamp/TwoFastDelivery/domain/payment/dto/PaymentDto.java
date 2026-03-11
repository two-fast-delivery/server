package nbcamp.TwoFastDelivery.domain.payment.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@NoArgsConstructor
public class PaymentDto {
    private UUID paymentId;
    private UUID orderId;
    private UUID userId;
    private String paymentKey;
    private String paymentStatus;
    private Integer totalAmount;
    private String method;
    private String cancelReason;

    public PaymentDto(UUID paymentId, UUID orderId, UUID userId, String paymentKey, String paymentStatus, Integer amount, String method, String cancelReason) {
        this.paymentId = paymentId;
        this.orderId = orderId;
        this.userId = userId;
        this.paymentKey = paymentKey;
        this.paymentStatus = paymentStatus;
        this.totalAmount = totalAmount;
        this.method = method;
        this.cancelReason = cancelReason;
    }
}
