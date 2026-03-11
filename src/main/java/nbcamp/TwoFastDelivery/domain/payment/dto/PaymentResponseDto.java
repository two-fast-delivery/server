package nbcamp.TwoFastDelivery.domain.payment.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PaymentResponseDto {
        private String paymentKey;
        private String orderId;
        private String method;      // 결제 수단 (카드, 가상계좌 등)
        private Integer totalAmount; // 최종 결제 금액
        private String status;      // 결제 상태 (DONE 등)
        private String requestedAt; // 결제 요청 시간
        private String approvedAt;  // 결제 승인 시간
}
