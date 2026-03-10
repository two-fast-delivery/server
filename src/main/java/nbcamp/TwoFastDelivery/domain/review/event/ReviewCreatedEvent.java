package nbcamp.TwoFastDelivery.domain.review.event;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@Getter
@AllArgsConstructor
public class ReviewCreatedEvent {
    private final UUID reviewId;
    private final UUID storeId;
}
