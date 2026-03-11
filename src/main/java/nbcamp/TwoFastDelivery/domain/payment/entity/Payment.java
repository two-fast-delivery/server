package nbcamp.TwoFastDelivery.domain.payment.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import nbcamp.TwoFastDelivery.global.common.BaseEntity;

import java.util.UUID;

/*
* - 결제 처리: 실제 연동 대신 데이터베이스 테이블에 성공 내역을 기록하여 처리한다.
- 관련글: https://docs.tosspayments.com/guides/v2/payment-widget/integration
* */

@Entity
@Getter
@Table(name="p_payment")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Payment extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID paymentId;

    //토스 결제키
    @Column(unique = true)
    private String paymentKey;

    //주문번호
    @Column(name = "order_id", nullable = false)
    private UUID orderId;

    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @Enumerated(EnumType.STRING)
    @Column(name="payment_status", nullable = false)
    private PaymentStatus paymentStatus;

    @Column(name="total_amount", nullable = false)
    private Integer totalAmount;

    //결제 수단(카드, 가상계좌, 간편결제 등)
    private String method;

    @Column(length=100)
    private String cancelReason;

    @Builder
    public Payment(String paymentKey, UUID orderId, UUID userId, Integer amount, PaymentStatus paymentStatus, String method) {
        this.paymentKey = paymentKey;
        this.orderId = orderId;
        this.userId = userId;
        this.totalAmount = totalAmount;
        this.paymentStatus = paymentStatus;
        this.method = method;
    }

    public void updatePaymentStatus(PaymentStatus paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

}
