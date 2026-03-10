package nbcamp.TwoFastDelivery.domain.product.dto.response;

import lombok.Builder;
import lombok.Getter;
import nbcamp.TwoFastDelivery.domain.product.entity.ProductOption;
import nbcamp.TwoFastDelivery.domain.product.entity.ProductOptionGroup;
import java.util.UUID;

public class ProductOptionResponse {

    @Getter
    @Builder
    public static class GroupInfo {
        private UUID id;
        private UUID productId;
        private String name;
        private Integer minSelection;
        private Integer maxSelection;
        private Integer sortOrder;

        public static GroupInfo fromEntity(ProductOptionGroup group) {
            return GroupInfo.builder()
                    .id(group.getId())
                    .productId(group.getProductId())
                    .name(group.getName())
                    .minSelection(group.getMinSelection())
                    .maxSelection(group.getMaxSelection())
                    .sortOrder(group.getSortOrder())
                    .build();
        }
    }

    @Getter
    @Builder
    public static class OptionInfo {
        private UUID id;
        private UUID groupId;
        private String name;
        private Integer extraPrice;
        private Integer stock;
        private String status;
        private Integer sortOrder;

        public static OptionInfo fromEntity(ProductOption option) {
            return OptionInfo.builder()
                    .id(option.getId())
                    .groupId(option.getGroupId())
                    .name(option.getName())
                    .extraPrice(option.getExtraPrice())
                    .stock(option.getStock())
                    .status(option.getStatus().name())
                    .sortOrder(option.getSortOrder())
                    .build();
        }
    }
}
