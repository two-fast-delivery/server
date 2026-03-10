package nbcamp.TwoFastDelivery.domain.product.dto.request;

import lombok.Getter;

public class ProductOptionRequest {

    @Getter
    public static class CreateOption {
        // Group properties (optional, for update)
        private String groupName;
        private Integer minSelection;
        private Integer maxSelection;

        // Option properties
        private String name;
        private Integer extraPrice;
        private Integer stock;
        private Integer sortOrder;
        private String status; // AVAILABLE, OUT_OF_STOCK, HIDDEN
    }
}
