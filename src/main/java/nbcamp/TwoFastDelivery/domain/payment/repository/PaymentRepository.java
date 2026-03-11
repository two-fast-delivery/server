package nbcamp.TwoFastDelivery.domain.payment.repository;

import nbcamp.TwoFastDelivery.domain.payment.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface PaymentRepository extends JpaRepository<Payment, UUID> {
    //주문번호로 결제 내역 조회
    Optional<Payment> findByOrderId(UUID orderId);

    //결제번호로 내역 조회
    Optional<Payment> findByPaymentKey(String paymentKey);
}
