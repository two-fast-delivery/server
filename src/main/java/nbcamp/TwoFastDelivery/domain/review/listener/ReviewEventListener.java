package nbcamp.TwoFastDelivery.domain.review.listener;

import lombok.RequiredArgsConstructor;
import nbcamp.TwoFastDelivery.domain.review.event.ReviewCreatedEvent;
import nbcamp.TwoFastDelivery.domain.review.event.ReviewDeletedEvent;
import nbcamp.TwoFastDelivery.domain.review.event.ReviewUpdatedEvent;
import nbcamp.TwoFastDelivery.domain.review.service.ReviewRatingUpdateService;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class ReviewEventListener {

    private final ReviewRatingUpdateService reviewRatingUpdateService;

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void handleReviewCreate(ReviewCreatedEvent event) {
        reviewRatingUpdateService.updateStoreRating(event.getStoreId());
    }

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void handleReviewUpdated(ReviewUpdatedEvent event) {
        reviewRatingUpdateService.updateStoreRating(event.getStoreId());
    }

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void handleReviewDeleted(ReviewDeletedEvent event) {
        reviewRatingUpdateService.updateStoreRating(event.getStoreId());
    }
}