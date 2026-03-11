package nbcamp.TwoFastDelivery.domain.payment.entity;

import lombok.Getter;

@Getter
public enum PaymentStatus {
        READY("결제 생성"),
        DONE("결제 완료"),
        CANCELED("결제 취소"),
        ABORTED("결제 실패/중단");

    private final String description;

    PaymentStatus(String description) {
        this.description = description;
    }
}
