package nbcamp.TwoFastDelivery.domain.product.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import nbcamp.TwoFastDelivery.global.common.BaseEntity;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "p_product_option")
public class ProductOption extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = true)
    private UUID groupId;

    @Column(nullable = false, length = 40)
    private String name;

    @Column(nullable = false)
    private Integer extraPrice;

    @Column(nullable = false)
    private Integer stock;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, columnDefinition = "productoptionstatus DEFAULT 'AVAILABLE'")
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    private ProductOptionStatus status;

    @Column(nullable = false)
    private Integer sortOrder;

    @Builder
    public ProductOption(UUID groupId, String name, Integer extraPrice, Integer stock, ProductOptionStatus status,
            Integer sortOrder) {
        this.groupId = groupId;
        this.name = name;
        this.extraPrice = extraPrice != null ? extraPrice : 0;
        this.stock = stock != null ? stock : 0;
        this.status = status != null ? status : ProductOptionStatus.AVAILABLE;
        this.sortOrder = sortOrder != null ? sortOrder : 0;
    }

    public void updateInfo(UUID groupId, String name, Integer extraPrice, Integer stock, Integer sortOrder) {
        this.groupId = groupId;
        this.name = name;
        this.extraPrice = extraPrice;
        this.stock = stock;
        this.sortOrder = sortOrder;
    }

    public void changeStatus(ProductOptionStatus status) {
        this.status = status;
    }

    public enum ProductOptionStatus {
        AVAILABLE, OUT_OF_STOCK, HIDDEN
    }
}
