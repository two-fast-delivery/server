package nbcamp.TwoFastDelivery.domain.order.controller;

import lombok.RequiredArgsConstructor;
import nbcamp.TwoFastDelivery.domain.order.dto.OrderDto;
import nbcamp.TwoFastDelivery.domain.order.entity.Order;
import nbcamp.TwoFastDelivery.domain.order.entity.OrderStatus;
import nbcamp.TwoFastDelivery.domain.order.service.OrderService;
import nbcamp.TwoFastDelivery.global.common.PageResponse;
import nbcamp.TwoFastDelivery.user.domain.user.User;
import nbcamp.TwoFastDelivery.user.domain.user.UserId;
import nbcamp.TwoFastDelivery.user.domain.user.UserRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

import static nbcamp.TwoFastDelivery.user.domain.user.QUserId.userId;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final UserRepository userRepository;

    @PostMapping
    public ResponseEntity<UUID> createOrder(
            @AuthenticationPrincipal UserDetails userDetails, // 1. 인증 정보 추가
            @RequestBody OrderDto orderDto) {

        User user = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("사용자 없음"));

        // 2. 서비스에 userId 넘겨서 저장하도록 수정 (서비스 로직 수정 필요)
        UUID orderId = orderService.createOrder(user.getUserId().getId(), orderDto);
        return ResponseEntity.ok(orderId);
    }

    @GetMapping
    public ResponseEntity<PageResponse<OrderDto>> getOrders(
            @AuthenticationPrincipal UserDetails userDetails, // 시큐리티 기본 객체 사용
            @RequestParam(required = false) Integer size,     // 추가됨
            @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {

        // 1. 유저 정보 조회 (이미 로그인 된 상태이므로 username으로 조회)
        // 팀원의 UserRepository가 findByUsername을 제공한다고 가정
        User user = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("사용자 없음"));

        // 2. 값 추출
        String role = user.getRole().name();
        UUID targetId = user.getUserId().getId(); // UserId 내부의 UUID 추출

        // 3. 페이지 처리 로직 (size 사용 가능해짐)
        int normalizedSize = PageResponse.normalizeSize(size != null ? size : pageable.getPageSize());
        Pageable customPageable = PageRequest.of(pageable.getPageNumber(), normalizedSize, pageable.getSort());

        // 4. 서비스 호출
        return ResponseEntity.ok(orderService.getOrders(targetId, role, customPageable));
    }

    @PostMapping("/{orderId}/cancel")
    public ResponseEntity<String> cancelOrder(
            @AuthenticationPrincipal UserDetails userDetails, // 3. 누가 취소하는지 확인
            @PathVariable UUID orderId) {

        User user = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("사용자 없음"));

        // 4. 서비스에서 "본인 주문인지" 확인하도록 userId 전달
        orderService.cancelOrder(orderId, user.getUserId().getId(), user.getRole().name());
        return ResponseEntity.ok("주문이 성공적으로 취소되었습니다.");
    }

    @PatchMapping("/{orderId}/status")
    public ResponseEntity<String> updateStatus(
            @AuthenticationPrincipal UserDetails userDetails, // 5. 상태 변경 권한 확인
            @PathVariable UUID orderId,
            @RequestParam OrderStatus status,
            @RequestAttribute UUID storeId) {

        User user = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("사용자 없음"));

        // 6. 상태 변경 권한(가게 점주인지 등) 확인 로직 필요
        orderService.updateOrderStatus(orderId, storeId, status, user.getRole().name());
        return ResponseEntity.ok("주문 상태가 " + status.getDescription() + "으로 변경되었습니다.");
    }
}