package nbcamp.TwoFastDelivery.domain.payment.controller;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import nbcamp.TwoFastDelivery.domain.payment.dto.PaymentConfirmDto;
import nbcamp.TwoFastDelivery.domain.payment.service.PaymentService;
import nbcamp.TwoFastDelivery.user.domain.user.User;
import nbcamp.TwoFastDelivery.user.domain.user.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/payments")
public class PaymentController {
    private final PaymentService paymentService;
    private final UserRepository userRepository;

    @PostMapping("/confirm")
    public ResponseEntity<String> confirmPayment(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody PaymentConfirmDto paymentConfirmDto) {
        try {
            User user = userRepository.findByUsername(userDetails.getUsername())
                    .orElseThrow(() -> new IllegalArgumentException("사용자 없음"));

            paymentService.confirmPayment(paymentConfirmDto, user.getUserId().getId(), user.getRole().name());
            return ResponseEntity.ok("결제가 성공적으로 완료되었습니다.");

        } catch (IllegalArgumentException e) {
            // 결제 금액 불일치 등 400 에러 유지
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (IllegalAccessError e) {
            // 권한 부족 시 403 에러
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        } catch (Exception e) {
            // 서버 오류 500 에러
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("결제 승인 중 오류가 발생했습니다.");
        }
    }

    @PostMapping("/cancel/{paymentKey}")
    public ResponseEntity<String> cancelPayment(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable("paymentKey") String paymentKey) {
        try {
            User user = userRepository.findByUsername(userDetails.getUsername())
                    .orElseThrow(() -> new IllegalArgumentException("사용자 없음"));

            paymentService.cancelPayment(paymentKey, user.getUserId().getId(), user.getRole().name());
            return ResponseEntity.ok("결제가 취소되었습니다.");

        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (IllegalAccessError e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("결제 취소 중 오류가 발생했습니다.");
        }
    }
}