package nbcamp.TwoFastDelivery.domain.payment.service;

import lombok.RequiredArgsConstructor;
import nbcamp.TwoFastDelivery.domain.order.entity.Order;
import nbcamp.TwoFastDelivery.domain.order.repository.OrderRepository;
import nbcamp.TwoFastDelivery.domain.payment.dto.PaymentConfirmDto;
import nbcamp.TwoFastDelivery.domain.payment.entity.Payment;
import nbcamp.TwoFastDelivery.domain.payment.entity.PaymentStatus;
import nbcamp.TwoFastDelivery.domain.payment.repository.PaymentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class PaymentService {
    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;

    // 결제 요청 및 승인
    public void confirmPayment(PaymentConfirmDto requestDto, UUID userId, String role) {
        Order order = orderRepository.findById(requestDto.getOrderId())
                .orElseThrow(() -> new IllegalArgumentException("주문 정보가 없습니다."));

        // 1. 소유자 및 권한 검증
        if (!order.getUserId().equals(userId) && !role.equals("MASTER")) {
            throw new IllegalAccessError("본인의 주문만 결제할 수 있습니다.");
        }

        // 2. 금액 검증
        if (!order.getAmount().getValue().equals(requestDto.getTotalAmount())) {
            throw new IllegalArgumentException("결제 금액이 일치하지 않습니다.");
        }

        // 3. 결제 성공 내역 DB 저장
        Payment payment = Payment.builder()
                .paymentKey(requestDto.getPaymentKey())
                .orderId(order.getId())
                .userId(order.getUserId())
                .amount(requestDto.getTotalAmount())
                .method("SUCCESS") // 성공 내역
                .paymentStatus(PaymentStatus.DONE)
                .build();

        paymentRepository.save(payment);
    }

    // 결제 취소
    public void cancelPayment(String paymentKey, UUID userId, String role) {
        Payment payment = paymentRepository.findByPaymentKey(paymentKey)
                .orElseThrow(() -> new IllegalArgumentException("결제 내역이 없습니다."));

        Order order = orderRepository.findById(payment.getOrderId())
                .orElseThrow(() -> new IllegalArgumentException("주문 정보를 찾을 수 없습니다."));

        // 4. 취소 시 본인 확인
        if (!payment.getUserId().equals(userId) && !role.equals("MASTER")) {
            throw new IllegalAccessError("본인의 결제 건만 취소할 수 있습니다.");
        }

        // 5. 상태 확인 및 취소
        if (payment.getPaymentStatus().equals(PaymentStatus.READY) ||
                payment.getPaymentStatus().equals(PaymentStatus.DONE)) {
            payment.updatePaymentStatus(PaymentStatus.CANCELED);
        } else {
            throw new IllegalArgumentException("취소 가능한 상태가 아닙니다.");
        }

        // 주문 상태도 함께 취소 처리
        order.cancel();
    }
}