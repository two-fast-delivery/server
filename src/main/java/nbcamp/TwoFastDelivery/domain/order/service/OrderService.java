package nbcamp.TwoFastDelivery.domain.order.service;

import lombok.RequiredArgsConstructor;
import nbcamp.TwoFastDelivery.domain.order.dto.OrderDto;
import nbcamp.TwoFastDelivery.domain.order.entity.Amount;
import nbcamp.TwoFastDelivery.domain.order.entity.Order;
import nbcamp.TwoFastDelivery.domain.order.entity.OrderDetail;
import nbcamp.TwoFastDelivery.domain.order.entity.OrderStatus;
import nbcamp.TwoFastDelivery.domain.order.repository.OrderRepository;
import nbcamp.TwoFastDelivery.global.common.PageResponse;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {
    private final OrderRepository orderRepository;

    //주문
    public UUID createOrder(OrderDto request) {
        System.out.println(">>> 서비스 진입! 상태값: " + request.getStatus());
        System.out.println(">>> 금액값: " + request.getAmount());
        //주문 상세 리스트
        List<OrderDetail> details = request.getItems().stream()
                .map(item -> new OrderDetail(
                item.getMenuId(),
                new Amount(item.getAmount()),
                item.getQuantity(),
                item.getMenuOption()
        ))
                .collect(Collectors.toList());

        //주문 총액 계산
        int total = details.stream()
                .mapToInt(d -> d.getAmount().getValue() * d.getQuantity())
                .sum();

        //주문 마스터 생성 및 연결
        Order order = new Order(
                request.getUserId(),
                request.getStoreId(),
                request.getAddress(),
                new Amount(request.getAmount()),
                request.getReq(),
                OrderStatus.valueOf(request.getStatus())
        );

        //상세 항목들 오더에 추가
        details.forEach(order::addOrderDetail);

        //저장
        return orderRepository.save(order).getId();
    }

    //페이지처리
    @Transactional(readOnly = true)
    public PageResponse<OrderDto> getOrders(UUID userId, Pageable pageable) {
        Page<Order> orderPage = orderRepository.findAllByUserId(userId, pageable);
        return PageResponse.from(orderPage, OrderDto::from);
    }

    //주문 취소
    public void cancelOrder(UUID orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("주문을 찾을 수 없습니다."));

        // 1. 주문 시간과 현재 시간 차이 계산
        long minutesElapsed = ChronoUnit.MINUTES.between(order.getCreatedAt(), LocalDateTime.now());

        // 예외 발생
        // 5분초과
        if (minutesElapsed >= 5) {
            throw new IllegalStateException("주문 생성 후 5분이 지나 취소할 수 없습니다.");
        }

        // 3. 상태 변경 (이미 취소된 상태인지 확인 등 추가 로직)
        order.cancel();
        orderRepository.save(order);
    }

    //주문 상태 변경
    @Transactional
    public void updateOrderStatus(UUID orderId, UUID storeId, OrderStatus newStatus) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("주문을 찾을 수 없습니다."));

        if(!order.getStoreId().equals(storeId)) {
            throw new IllegalAccessError("해당 가게의 주문이 아닙니다.");
        }

        order.updateStatus(newStatus);
    }

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
}
