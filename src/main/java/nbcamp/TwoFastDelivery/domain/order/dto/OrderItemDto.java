package nbcamp.TwoFastDelivery.domain.order.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import nbcamp.TwoFastDelivery.domain.order.entity.OrderDetail;

import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemDto {
    private UUID menuId;
    private Integer quantity;
    private Integer amount;
    private String menuOption;

    public static OrderItemDto from(OrderDetail orderDetail) {
        return new OrderItemDto(
                orderDetail.getMenuId(),
                orderDetail.getQuantity(),
                orderDetail.getAmount().getValue(),
                orderDetail.getMenuOption()
        );
    }
}
