package nbcamp.TwoFastDelivery.domain.order.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
/*
 * 6. 주문 상태 변경은 OWNER만 처리 가능
 *  - 주문 요청 -> 조리중 -> 조리완료 -> 배달 중 -> 배달 완료
* */

@Getter
@RequiredArgsConstructor
public enum OrderStatus {
    ORDER_RECEIVED("주문 접수"),
    PAYMENT_COMPLETED("결제 완료"),
    COOKING("요리중"),
    IN_DELIVERY("배달중"),
    DELIVERY_COMPLETED("배달 완료"),
    CANCELLED("주문 취소");

    private final String description;
}
