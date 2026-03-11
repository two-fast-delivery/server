package nbcamp.TwoFastDelivery.store.application.dto;

import java.util.UUID;

import lombok.Getter;
import lombok.Builder;

@Getter
@Builder

public class StoreSummaryResponse {
    
    private final UUID id;
    private final String name;
    private final String address;
    private final String thumbnailUrl;
    private final String categoryName;
    private final Float avgRating;
    private final Integer reviewCount;
}
