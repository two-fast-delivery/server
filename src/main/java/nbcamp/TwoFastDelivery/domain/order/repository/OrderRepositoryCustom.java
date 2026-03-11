package nbcamp.TwoFastDelivery.domain.order.repository;

import nbcamp.TwoFastDelivery.domain.order.dto.OrderSearchCondition;
import nbcamp.TwoFastDelivery.domain.order.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface OrderRepositoryCustom{
    Page<Order> searchOrders(OrderSearchCondition condition, Pageable pageable);
}