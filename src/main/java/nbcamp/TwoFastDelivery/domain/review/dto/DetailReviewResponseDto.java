package nbcamp.TwoFastDelivery.domain.review.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import nbcamp.TwoFastDelivery.domain.review.enums.ReviewStatus;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@AllArgsConstructor
public class DetailReviewResponseDto {
    private UUID reviewId;
    private ReviewStatus Status;
    private UUID storeId;
    private UUID userId;
    private Integer rating;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
