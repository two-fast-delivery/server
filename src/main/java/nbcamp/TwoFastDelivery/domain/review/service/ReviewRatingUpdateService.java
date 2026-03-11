package nbcamp.TwoFastDelivery.domain.review.service;

import lombok.RequiredArgsConstructor;
import nbcamp.TwoFastDelivery.domain.review.enums.ReviewStatus;
import nbcamp.TwoFastDelivery.domain.review.repository.ReviewRepository;
import nbcamp.TwoFastDelivery.global.exception.CustomException;
import nbcamp.TwoFastDelivery.global.exception.ErrorCode;
import nbcamp.TwoFastDelivery.store.domain.Store;
import nbcamp.TwoFastDelivery.store.domain.StoreRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ReviewRatingUpdateService {

    private final ReviewRepository reviewRepository;
    private final StoreRepository storeRepository;

    @Transactional
    public void updateStoreRating(UUID storeId) {
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND));

        Double avgRating = reviewRepository.findAverageRatingByStoreIdAndStatus(storeId, ReviewStatus.ACTIVE);
        long reviewCount = reviewRepository.countByStoreIdAndStatus(storeId, ReviewStatus.ACTIVE);

        if (avgRating == null || reviewCount == 0) {
            store.updateRating(0f, 0);
            storeRepository.save(store);
            return;
        }

        float roundedAvg = Math.round(avgRating * 10) / 10.0f;
        store.updateRating(roundedAvg, (int) reviewCount);
        storeRepository.save(store);
    }
}