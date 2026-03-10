package nbcamp.TwoFastDelivery.domain.order.repository;

import nbcamp.TwoFastDelivery.domain.order.entity.Order;
import nbcamp.TwoFastDelivery.domain.order.entity.OrderDetail;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface OrderRepository extends JpaRepository<Order, UUID> {
    public interface OrderDetailRepository extends JpaRepository<OrderDetail, UUID> {}
    Page<Order> findAllByUserId(UUID userId, Pageable pageable);
}
