package nbcamp.TwoFastDelivery.domain.review.repository;

import nbcamp.TwoFastDelivery.domain.review.entity.Review;
import nbcamp.TwoFastDelivery.domain.review.enums.ReviewStatus;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.UUID;

public interface ReviewRepository extends JpaRepository<Review, UUID> {

    boolean existsByOrderId(Long orderId);

    Page<Review> findReviewByStoreId(Long storeId, ReviewStatus status, Pageable pageable);
    Page<Review> findReviewByUserId (Long userId, Pageable pageable);

    //평점 계산
    @Query("""
            select avg(r.rating)
            from Review r
            where r.storeId = :storeId
              and r.status = nbcamp.TwoFastDelivery.domain.review.enums.ReviewStatus.ACTIVE
            """)
    Double findAverageRatingByStoreId(@Param("storeId") UUID storeId);

    long countByStoreIdAndStatus(UUID storeId, ReviewStatus status);
}
