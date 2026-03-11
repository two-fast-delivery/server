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

@Service
@RequiredArgsConstructor
@Transactional
public class PaymentService {
    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;

    //결제 요청 및 승인
    public void confirmPayment(PaymentConfirmDto requestDto) {
        // 1. 주문 데이터 확인
        Order order = orderRepository.findById(requestDto.getOrderId())
                .orElseThrow(() -> new IllegalArgumentException("주문 정보가 없습니다."));

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
                .method("SUCCESS") // 테스트용 값
                .paymentStatus(PaymentStatus.DONE)
                .build();

        paymentRepository.save(payment);
    }

    //결제 취소
    public void cancelPayment(String paymentKey) {
        Payment payment = paymentRepository.findByPaymentKey(paymentKey)
                .orElseThrow(() -> new IllegalArgumentException("결제 취소 가능한 결제 내역이 없습니다."));

        Order order = orderRepository.findById(payment.getOrderId())
                .orElseThrow(() -> new IllegalArgumentException("해결할 결제건이 없습니다."));

        if (payment.getPaymentStatus().equals(PaymentStatus.READY)||payment.getPaymentStatus().equals(PaymentStatus.DONE)) {
            payment.updatePaymentStatus(PaymentStatus.CANCELED);
        }else{
            throw new IllegalArgumentException("결제 취소 가능한 결제 내역이 없습니다.");
        }

        paymentRepository.save(payment);
        order.cancel();
    }
}
