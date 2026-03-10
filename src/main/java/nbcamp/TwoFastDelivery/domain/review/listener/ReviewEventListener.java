package nbcamp.TwoFastDelivery.domain.review.listener;

import lombok.RequiredArgsConstructor;
import nbcamp.TwoFastDelivery.domain.review.enums.ReviewStatus;
import nbcamp.TwoFastDelivery.domain.review.event.ReviewCreatedEvent;
import nbcamp.TwoFastDelivery.domain.review.event.ReviewDeletedEvent;
import nbcamp.TwoFastDelivery.domain.review.event.ReviewUpdatedEvent;
import nbcamp.TwoFastDelivery.domain.review.repository.ReviewRepository;
import nbcamp.TwoFastDelivery.domain.store.domain.Store;
import nbcamp.TwoFastDelivery.domain.store.domain.StoreRepository;
import nbcamp.TwoFastDelivery.global.exception.CustomException;
import nbcamp.TwoFastDelivery.global.exception.ErrorCode;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ReviewEventListener {

    private final ReviewRepository reviewRepository;
    private final StoreRepository storeRepository;

    @TransactionalEventListener
    public void handleReviewCreate(ReviewCreatedEvent event) {
        updateStoreRating(event.getStoreId());
    }

    @TransactionalEventListener
    public void handleReviewUpdated(ReviewUpdatedEvent event) {
        updateStoreRating(event.getStoreId());
    }

    @TransactionalEventListener
    public void handleReviewDeleted(ReviewDeletedEvent event) {
        updateStoreRating(event.getStoreId());
    }

    private void updateStoreRating(UUID storeId) {

        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND));

        Double avgRating = reviewRepository.findAverageRatingByStoreId(storeId);
        long reviewCount = reviewRepository.countByStoreIdAndStatus(storeId, ReviewStatus.ACTIVE);

        if (avgRating == null || reviewCount == 0) {
            store.updateRating(0f, 0);
            return;
        }

        float roundedAvg = Math.round(avgRating * 10) / 10.0f;

        store.updateRating(roundedAvg, (int) reviewCount);
    }
}