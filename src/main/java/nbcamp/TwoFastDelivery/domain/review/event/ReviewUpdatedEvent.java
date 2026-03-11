package nbcamp.TwoFastDelivery.domain.review.event;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@Getter
@AllArgsConstructor
public class ReviewUpdatedEvent {
    private final UUID reviewId;
    private final UUID storeId;
}
