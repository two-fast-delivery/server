package nbcamp.TwoFastDelivery.domain.payment.controller;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import nbcamp.TwoFastDelivery.domain.payment.dto.PaymentConfirmDto;
import nbcamp.TwoFastDelivery.domain.payment.service.PaymentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/payments")
public class PaymentController {
    private final PaymentService paymentService;

    // 결제 승인 API
    @PostMapping("/confirm")
    public ResponseEntity<String> confirmPayment(@RequestBody PaymentConfirmDto paymentConfirmDto) {
        try {
            paymentService.confirmPayment(paymentConfirmDto);
            return ResponseEntity.ok("결제가 성공적으로 완료되었습니다.");
        } catch (IllegalArgumentException e) {
            // 결제 금액 불일치 등 예외 처리
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            // 서버 오류 예외 처리
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("결제 승인 중 오류가 발생했습니다.");
        }
    }

    //결제 취소 API
    @PostMapping("/cancel/{paymentKey}")
    public ResponseEntity<String> cancelPayment(@PathVariable("paymentKey") String paymentKey) {
        paymentService.cancelPayment(paymentKey);
        return ResponseEntity.ok("결제가 취소되었습니다.");
    }

}
