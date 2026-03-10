package nbcamp.TwoFastDelivery.domain.product.dto.response;

import lombok.Builder;
import lombok.Getter;
import nbcamp.TwoFastDelivery.domain.product.entity.Product;

import java.time.LocalDateTime;
import java.util.UUID;

public class ProductResponse {

    @Getter
    @Builder
    @lombok.NoArgsConstructor
    @lombok.AllArgsConstructor(access = lombok.AccessLevel.PRIVATE)
    public static class Info {
        private UUID id;
        private UUID storeId;
        private UUID productGroupId;
        private String productGroupName;
        private String name;
        private String description;
        private String prompt;
        private String imageUrl;
        private Integer price;
        private String status;
        private Integer stock;
        private Integer maxOrderQuantity;
        private Integer sortOrder;
        private Boolean isAiGenerated;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        public static Info fromEntity(Product product, String productGroupName) {
            return Info.builder()
                    .id(product.getId())
                    .storeId(product.getStoreId())
                    .productGroupId(product.getProductGroupId())
                    .productGroupName(productGroupName)
                    .name(product.getName())
                    .description(product.getDescription())
                    .prompt(product.getPrompt())
                    .imageUrl(product.getImageUrl())
                    .price(product.getPrice())
                    .status(product.getStatus().name())
                    .stock(product.getStock())
                    .maxOrderQuantity(product.getMaxOrderQuantity())
                    .sortOrder(product.getSortOrder())
                    .isAiGenerated(product.getIsAiGenerated())
                    .createdAt(product.getCreatedAt())
                    .updatedAt(product.getUpdatedAt())
                    .build();
        }
    }
}
