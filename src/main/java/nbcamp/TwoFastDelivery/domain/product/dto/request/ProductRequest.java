package nbcamp.TwoFastDelivery.domain.product.dto.request;

import lombok.Getter;
import java.util.UUID;

public class ProductRequest {

    @Getter
    public static class Create {
        private UUID productGroupId;
        private String productGroupName;
        private String name;
        private String description;
        private String prompt;
        private String imageUrl;
        private Integer price;
        private Integer stock;
        private Integer maxOrderQuantity;
        private Integer sortOrder;
        private Boolean isAiGenerated;
    }

    @Getter
    public static class Update {
        private UUID productGroupId;
        private String productGroupName;
        private String name;
        private String description;
        private String prompt;
        private String imageUrl;
        private Integer price;
        private Integer stock;
        private Integer maxOrderQuantity;
        private Integer sortOrder;
        private Boolean isAiGenerated;
    }

    @Getter
    public static class ChangeStatus {
        private String status; // SALE, OUT_OF_STOCK, HIDDEN, DISCONTINUED
    }

    @Getter
    public static class GenerateDescription {
        private String prompt;
        private String imageUrl;
    }
}
