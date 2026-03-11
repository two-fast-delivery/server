package nbcamp.TwoFastDelivery.domain.review.repository;

import nbcamp.TwoFastDelivery.domain.review.entity.Review;
import nbcamp.TwoFastDelivery.domain.review.enums.ReviewStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.UUID;

public interface ReviewRepository extends JpaRepository<Review, UUID> {

    boolean existsByOrderId(UUID orderId);

    Page<Review> findReviewByStoreIdAndStatus(UUID storeId, ReviewStatus status, Pageable pageable);
    Page<Review> findReviewByUserId (UUID userId, Pageable pageable);

    //평점 계산
    @Query("""
        select avg(r.rating)
        from Review r
        where r.storeId = :storeId
          and r.status = :status
        """)
    Double findAverageRatingByStoreIdAndStatus(@Param("storeId") UUID storeId,
                                               @Param("status") ReviewStatus status);

    long countByStoreIdAndStatus(UUID storeId, ReviewStatus status);
}
