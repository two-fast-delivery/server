package nbcamp.TwoFastDelivery.domain.order.service;

import lombok.RequiredArgsConstructor;
import nbcamp.TwoFastDelivery.domain.order.dto.OrderDto;
import nbcamp.TwoFastDelivery.domain.order.entity.Amount;
import nbcamp.TwoFastDelivery.domain.order.entity.Order;
import nbcamp.TwoFastDelivery.domain.order.entity.OrderDetail;
import nbcamp.TwoFastDelivery.domain.order.entity.OrderStatus;
import nbcamp.TwoFastDelivery.domain.order.repository.OrderRepository;
import nbcamp.TwoFastDelivery.domain.product.entity.Product;
import nbcamp.TwoFastDelivery.domain.product.repository.ProductRepository;
import nbcamp.TwoFastDelivery.global.common.PageResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.awt.*;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

    // 주문 생성 시 유저 ID를 직접 받아서 사용
    public UUID createOrder(UUID userId, OrderDto request) {
        // 1. 주문 상세 아이템들에 대한 ID 목록 추출
        List<UUID> productIds = request.getItems().stream()
                .map(item -> item.getMenuId()) // 또는 item.getProductId()
                .toList();
        Map<UUID, Integer> productPriceMap = productRepository.findAllById(productIds).stream()
                .collect(Collectors.toMap(Product::getId, Product::getPrice));

        // 3. 주문 상세 생성 및 금액 검증
        int calculatedTotal = 0;
        for (var item : request.getItems()) {
            Integer actualPrice = productPriceMap.get(item.getMenuId());
            if (actualPrice == null) throw new IllegalArgumentException("존재하지 않는 메뉴입니다.");

            calculatedTotal += (actualPrice * item.getQuantity());
        }

        // 4. 클라이언트가 보낸 총액과 DB 기반 계산 총액 비교
        if (request.getAmount() != calculatedTotal) {
            throw new IllegalArgumentException("결제 금액이 일치하지 않습니다. (요청액: " + request.getAmount() + ", 계산액: " + calculatedTotal + ")");
        }

        // 4. 주문 마스터 객체 먼저 생성
        Order order = new Order(
                userId,
                request.getStoreId(),
                request.getAddress(),
                new Amount(request.getAmount()),
                request.getReq(),
                OrderStatus.valueOf(request.getStatus())
        );

        // 5. 상세 리스트 생성 및 연관관계 설정
        request.getItems().forEach(item -> {
            OrderDetail detail = new OrderDetail(
                    item.getMenuId(),
                    new Amount(item.getAmount()),
                    item.getQuantity(),
                    item.getMenuOption()
            );
            // addOrderDetail 메서드 호출
            order.addOrderDetail(detail);
        });

        return orderRepository.save(order).getId();
    }

    // 주문 취소 시 권한 검증 추가
    public void cancelOrder(UUID orderId, UUID userId, String role) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("주문을 찾을 수 없습니다."));

        // 권한 체크: 본인 주문이거나 관리자(MASTER/MANAGER)인 경우만 허용
        if (!order.getUserId().equals(userId) && !role.equals("MASTER") && !role.equals("MANAGER")) {
            throw new IllegalAccessError("본인의 주문만 취소할 수 있습니다.");
        }

        long minutesElapsed = ChronoUnit.MINUTES.between(order.getCreatedAt(), LocalDateTime.now());
        if (minutesElapsed >= 5) {
            throw new IllegalStateException("주문 생성 후 5분이 지나 취소할 수 없습니다.");
        }

        order.cancel();
    }

    // 상태 변경 시 권한 검증 추가
    public void updateOrderStatus(UUID orderId, UUID storeId, OrderStatus newStatus, String role) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("주문을 찾을 수 없습니다."));

        // 권한 체크: 관리자이거나 해당 가게 주인(OWNER)만 가능
        boolean isOwner = role.equals("OWNER") && order.getStoreId().equals(storeId);
        boolean isAdmin = role.equals("MASTER") || role.equals("MANAGER");

        if (!isOwner && !isAdmin) {
            throw new IllegalAccessError("권한이 없습니다.");
        }

        order.updateStatus(newStatus);
    }

    @Transactional(readOnly = true)
    public PageResponse<OrderDto> getOrders(UUID targetId, String role, Pageable pageable) {
        Page<Order> orderPage;

        // 1. 권한에 따른 조회 범위 설정
        switch (role) {
            case "MASTER", "MANAGER" -> orderPage = orderRepository.findAll(pageable);
            case "OWNER" -> orderPage = orderRepository.findAllByStoreId(targetId, pageable);
            case "CUSTOMER" -> orderPage = orderRepository.findAllByUserId(targetId, pageable);
            default -> throw new IllegalArgumentException("유효하지 않은 권한입니다.");
        }

        // 2. PageResponse로 변환하여 반환
        return PageResponse.from(orderPage, OrderDto::from);
    }
}