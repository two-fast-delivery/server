package nbcamp.TwoFastDelivery.domain.order.service;

import lombok.RequiredArgsConstructor;
import nbcamp.TwoFastDelivery.domain.order.dto.OrderDto;
import nbcamp.TwoFastDelivery.domain.order.entity.Amount;
import nbcamp.TwoFastDelivery.domain.order.entity.Order;
import nbcamp.TwoFastDelivery.domain.order.entity.OrderDetail;
import nbcamp.TwoFastDelivery.domain.order.entity.OrderStatus;
import nbcamp.TwoFastDelivery.domain.order.repository.OrderRepository;
import nbcamp.TwoFastDelivery.domain.payment.entity.Payment;
import nbcamp.TwoFastDelivery.domain.payment.entity.PaymentStatus;
import nbcamp.TwoFastDelivery.domain.payment.repository.PaymentRepository;
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
    private final PaymentRepository paymentRepository;

    // 주문 생성 (금액 검증 및 주문 시점 가격 저장)
    public UUID createOrder(UUID userId, OrderDto request) {
        List<UUID> productIds = request.getItems().stream().map(item -> item.getMenuId()).toList();
        Map<UUID, Integer> productPriceMap = productRepository.findAllById(productIds).stream()
                .collect(Collectors.toMap(Product::getId, Product::getPrice));

        int calculatedTotal = 0;
        for (var item : request.getItems()) {
            Integer actualPrice = productPriceMap.get(item.getMenuId());
            if (actualPrice == null) throw new IllegalArgumentException("존재하지 않는 메뉴입니다.");
            calculatedTotal += (actualPrice * item.getQuantity());
        }

        if (request.getAmount() != calculatedTotal) {
            throw new IllegalArgumentException("결제 금액이 일치하지 않습니다.");
        }

        Order order = new Order(userId, request.getStoreId(), request.getAddress(),
                new Amount(request.getAmount()), request.getReq(), OrderStatus.valueOf(request.getStatus()));

        request.getItems().forEach(item -> {
            Integer orderPrice = productPriceMap.get(item.getMenuId());
            order.addOrderDetail(new OrderDetail(item.getMenuId(), new Amount(orderPrice),
                    item.getQuantity(), item.getMenuOption()));
        });

        return orderRepository.save(order).getId();
    }

    // 주문 조회 (권한별 분기)
    @Transactional(readOnly = true)
    public PageResponse<OrderDto> getOrders(UUID targetId, String role, Pageable pageable) {
        Page<Order> orderPage;
        switch (role) {
            case "MASTER", "MANAGER" -> orderPage = orderRepository.findAll(pageable);
            case "OWNER" -> orderPage = orderRepository.findAllByStoreId(targetId, pageable);
            case "CUSTOMER" -> orderPage = orderRepository.findAllByUserId(targetId, pageable);
            default -> throw new IllegalArgumentException("유효하지 않은 권한입니다.");
        }
        return PageResponse.from(orderPage, OrderDto::from);
    }

    // 주문 취소 (본인/관리자 권한 검증 및 5분 제한)
    public void cancelOrder(UUID orderId, UUID userId, String role) {
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new IllegalArgumentException("주문을 찾을 수 없습니다."));

        if (!order.getUserId().equals(userId) && !role.equals("MASTER") && !role.equals("MANAGER")) {
            throw new IllegalAccessError("본인의 주문만 취소할 수 있습니다.");
        }

        long minutesElapsed = ChronoUnit.MINUTES.between(order.getCreatedAt(), LocalDateTime.now());
        if (minutesElapsed >= 5) {
            throw new IllegalStateException("주문 생성 후 5분이 지나 취소할 수 없습니다.");
        }

        order.cancel();
        orderRepository.save(order);
    }

    // 주문 상태 변경 (가게 주인/관리자 권한 검증)
    public void updateOrderStatus(UUID orderId, UUID storeId, OrderStatus newStatus, String role) {
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new IllegalArgumentException("주문을 찾을 수 없습니다."));

        if (!order.getStoreId().equals(storeId)) {
            throw new IllegalAccessError("해당 가게의 주문이 아닙니다.");
        }

        boolean isOwner = role.equals("OWNER");
        boolean isAdmin = role.equals("MASTER") || role.equals("MANAGER");

        if (!isOwner && !isAdmin) {
            throw new IllegalAccessError("권한이 없습니다.");
        }

        order.updateStatus(newStatus);
    }
}