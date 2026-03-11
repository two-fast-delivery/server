package nbcamp.TwoFastDelivery.domain.review.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@AllArgsConstructor
public class CreateReviewResponseDto {
    private UUID reviewId;
    private Integer rating;
    private String content;
    private LocalDateTime createdAt;
}
