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
@Table(name = "p_product_option_group")
public class ProductOptionGroup extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private UUID productId;

    @Column(nullable = false, length = 40)
    private String name;

    @Column(nullable = false)
    private Integer minSelection;

    private Integer maxSelection;

    @Column(nullable = false)
    private Integer sortOrder;

    @Builder
    public ProductOptionGroup(UUID productId, String name, Integer minSelection, Integer maxSelection,
            Integer sortOrder) {
        this.productId = productId;
        this.name = name;
        this.minSelection = minSelection != null ? minSelection : 0;
        this.maxSelection = maxSelection;
        this.sortOrder = sortOrder != null ? sortOrder : 0;
    }

    public void updateInfo(String name, Integer minSelection, Integer maxSelection, Integer sortOrder) {
        this.name = name;
        this.minSelection = minSelection;
        this.maxSelection = maxSelection;
        this.sortOrder = sortOrder;
    }
}
