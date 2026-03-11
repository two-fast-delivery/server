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
    private final UserRepository userRepository; // 유저 정보 조회용

    @PostMapping("/confirm")
    public ResponseEntity<String> confirmPayment(
            @AuthenticationPrincipal UserDetails userDetails, // 1. 유저 정보 추가
            @RequestBody PaymentConfirmDto paymentConfirmDto) {

        User user = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("사용자 없음"));

        // 2. 서비스에 userId 넘겨서 검증 수행
        paymentService.confirmPayment(paymentConfirmDto, user.getUserId().getId(), user.getRole().name());
        return ResponseEntity.ok("결제가 성공적으로 완료되었습니다.");
    }

    @PostMapping("/cancel/{paymentKey}")
    public ResponseEntity<String> cancelPayment(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable("paymentKey") String paymentKey) {

        User user = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("사용자 없음"));

        // 3. 결제 취소 시에도 유저 권한 체크
        paymentService.cancelPayment(paymentKey, user.getUserId().getId(), user.getRole().name());
        return ResponseEntity.ok("결제가 취소되었습니다.");
    }
}
