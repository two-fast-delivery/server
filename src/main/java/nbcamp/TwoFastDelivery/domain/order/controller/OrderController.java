package nbcamp.TwoFastDelivery.domain.order.controller;

import lombok.RequiredArgsConstructor;
import nbcamp.TwoFastDelivery.domain.order.dto.OrderDto;
import nbcamp.TwoFastDelivery.domain.order.entity.Order;
import nbcamp.TwoFastDelivery.domain.order.entity.OrderStatus;
import nbcamp.TwoFastDelivery.domain.order.service.OrderService;
import nbcamp.TwoFastDelivery.global.common.PageResponse;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<UUID> createOrder(@RequestBody OrderDto orderDto) {
        UUID orderId = orderService.createOrder(orderDto);
        return ResponseEntity.ok(orderId);
    }

    @GetMapping
    public ResponseEntity<PageResponse<OrderDto>> getOrders(
    @RequestParam UUID userId,
    @RequestParam(required = false) Integer size, // 파라미터로 size를 따로 받음
    @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {

        // 1. PageResponse에 정의된 규칙대로 size 정규화 (10, 30, 50만 허용)
        int normalizedSize = PageResponse.normalizeSize(size);

        // 2. Pageable을 다시 생성 (PageRequest 사용)
        Pageable customPageable = PageRequest.of(pageable.getPageNumber(), normalizedSize, pageable.getSort());

        // 3. 서비스 호출
        return ResponseEntity.ok(orderService.getOrders(userId, customPageable));
    }

    @PostMapping("/{orderId}/cancel")
    public ResponseEntity<String> cancelOrder(@PathVariable UUID orderId) {
        try {
            orderService.cancelOrder(orderId);
            return ResponseEntity.ok("주문이 성공적으로 취소되었습니다.");
        } catch(IllegalStateException e){
            //5분이 지났을 때
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PatchMapping("/{orderId}/status")
    public ResponseEntity<String> updateStatus(@PathVariable UUID orderId, @RequestParam OrderStatus status, @RequestAttribute UUID storeId) {
        try {
            orderService.updateOrderStatus(orderId, storeId, status);
            return ResponseEntity.ok("주문 상태가 " + status.getDescription() + "으로 변경되었습니다.");
        }catch (IllegalStateException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
