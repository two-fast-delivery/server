package nbcamp.TwoFastDelivery.domain.order.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import nbcamp.TwoFastDelivery.domain.address.domain.Address;
import nbcamp.TwoFastDelivery.domain.order.entity.Order;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
public class OrderDto {
    private UUID orderId;
    private UUID userId;
    private UUID storeId;
    private String address;
    private String req;
    private List<OrderItemDto> items;
    private Integer amount;
    private String status;
    private LocalDateTime createdAt;

    public OrderDto(UUID orderId, String status, Integer amount, LocalDateTime createdAt, String address, UUID storeId, List<OrderItemDto> items, UUID userId) {
        this.orderId = orderId;
        this.status = status;
        this.amount = amount;
        this.createdAt = createdAt;
        this.address = address;
        this.userId = userId;
        this.storeId = storeId;
        this.items = items;
    }

    public static OrderDto from(Order order) {
        // 1. 엔티티 리스트를 DTO 리스트로 변환
        List<OrderItemDto> itemDtos = order.getOrderDetails().stream()
                .map(OrderItemDto::from)
                .collect(Collectors.toList());

        // 2. 생성자 호출 (쉼표와 파라미터 개수를 정확히 맞춤)
        return new OrderDto(
                order.getId(),
                order.getStatus().name(),
                order.getAmount().getValue(),
                order.getCreatedAt(),
                order.getAddress(),
                order.getStoreId(),
                itemDtos,        // 쉼표 추가!
                order.getUserId()
        );
    }

}
