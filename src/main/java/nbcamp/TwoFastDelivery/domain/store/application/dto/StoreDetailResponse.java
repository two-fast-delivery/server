package nbcamp.TwoFastDelivery.domain.store.application.dto;

import java.util.UUID;

import lombok.Getter;
import lombok.Builder;


@Getter
@Builder
public class StoreDetailResponse {
    private final UUID id;
    private final UUID userId;
    private final String name;
    private final String address;
    private final String phone;
    private final UUID categoryId;
    private final String categoryName;
    private final String thumbnailUrl;
    private final String openTime;
    private final String closeTime;
    private final String description;
    private final String status;
    private final Byte avgRating;
    private final Integer reviewCount;
}
