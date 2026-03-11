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
@Table(name = "p_product")
public class Product extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private UUID storeId;

    @Column(nullable = true)
    private UUID productGroupId;

    @Column(nullable = false, length = 40)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(columnDefinition = "TEXT")
    private String prompt;

    @Column(length = 512)
    private String imageUrl;

    @Column(nullable = false)
    private Integer price;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, columnDefinition = "productstatus DEFAULT 'SALE'")
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    private ProductStatus status;

    @Column(nullable = false)
    private Integer stock;

    private Integer maxOrderQuantity;

    @Column(nullable = false)
    private Integer sortOrder;

    @Column(nullable = false)
    private Boolean isAiGenerated = false;

    @Builder
    public Product(UUID storeId, UUID productGroupId, String name, String description, String prompt, String imageUrl,
            Integer price, ProductStatus status, Integer stock, Integer maxOrderQuantity, Integer sortOrder,
            Boolean isAiGenerated) {
        this.storeId = storeId;
        this.productGroupId = productGroupId;
        this.name = name;
        this.description = description;
        this.prompt = prompt;
        this.imageUrl = imageUrl;
        this.price = price;
        this.status = status != null ? status : ProductStatus.SALE;
        this.stock = stock != null ? stock : 0;
        this.maxOrderQuantity = maxOrderQuantity;
        this.sortOrder = sortOrder != null ? sortOrder : 0;
        this.isAiGenerated = isAiGenerated != null ? isAiGenerated : false;
    }

    public void updateInfo(UUID productGroupId, String name, String description, String prompt, String imageUrl,
            Integer price, Integer stock, Integer maxOrderQuantity, Integer sortOrder, Boolean isAiGenerated) {
        this.productGroupId = productGroupId;
        this.name = name;
        this.description = description;
        this.prompt = prompt;
        this.imageUrl = imageUrl;
        this.price = price;
        this.stock = stock;
        this.maxOrderQuantity = maxOrderQuantity;
        this.sortOrder = sortOrder;
        if (isAiGenerated != null) {
            this.isAiGenerated = isAiGenerated;
        }
    }

    public void changeStatus(ProductStatus status) {
        this.status = status;
    }

    public enum ProductStatus {
        SALE, OUT_OF_STOCK, HIDDEN, DISCONTINUED
    }
}
