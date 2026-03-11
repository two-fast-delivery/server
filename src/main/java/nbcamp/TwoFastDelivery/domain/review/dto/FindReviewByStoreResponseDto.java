package nbcamp.TwoFastDelivery.domain.review.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import nbcamp.TwoFastDelivery.domain.review.entity.Review;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@AllArgsConstructor
@Builder
public class FindReviewByStoreResponseDto {
    private UUID reviewId;
    private int rating;
    private String content;
    private LocalDateTime createdAt;

    public static FindReviewByStoreResponseDto from(Review review) {
        return FindReviewByStoreResponseDto.builder()
                .reviewId(review.getId())
                .rating(review.getRating())
                .content(review.getContent())
                .createdAt(review.getCreatedAt())
                .build();
    }
}
