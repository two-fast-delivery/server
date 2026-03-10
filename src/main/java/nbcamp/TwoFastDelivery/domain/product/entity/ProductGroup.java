package nbcamp.TwoFastDelivery.domain.product.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import nbcamp.TwoFastDelivery.global.common.BaseEntity;

import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "p_product_group")
public class ProductGroup extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private UUID storeId;

    @Column(nullable = false, length = 40)
    private String name;

    @Column(nullable = false)
    private Integer sortOrder;

    @Builder
    public ProductGroup(UUID storeId, String name, Integer sortOrder) {
        this.storeId = storeId;
        this.name = name;
        this.sortOrder = sortOrder != null ? sortOrder : 0;
    }

    public void updateInfo(String name, Integer sortOrder) {
        this.name = name;
        this.sortOrder = sortOrder;
    }
}
