package nbcamp.TwoFastDelivery.domain.review.repository;

import nbcamp.TwoFastDelivery.domain.review.entity.Review;
import nbcamp.TwoFastDelivery.domain.review.enums.ReviewStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ReviewRepository extends JpaRepository<Review, UUID> {

    boolean existsByOrderId(Long orderId);

    Page<Review> findReviewByStoreId(Long storeId, ReviewStatus status, Pageable pageable);
    Page<Review> findReviewByUserId (Long userId, Pageable pageable);
}
