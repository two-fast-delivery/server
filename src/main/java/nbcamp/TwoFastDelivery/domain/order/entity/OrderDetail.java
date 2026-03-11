package nbcamp.TwoFastDelivery.domain.order.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.UUID;
import java.util.List;

@Entity
@Table(name="p_detail")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="order_id")
    private Order order;

    public OrderDetail(UUID menuId, Amount amount, Integer quantity, String menuOption) {
        this.menuId = menuId;
        this.amount = amount;
        this.quantity = quantity;
        this.menuOption = menuOption;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    private UUID menuId;

    @Embedded
    private Amount amount;

    private Integer quantity;

    @Column(name = "menu_option")
    private String menuOption;

}
