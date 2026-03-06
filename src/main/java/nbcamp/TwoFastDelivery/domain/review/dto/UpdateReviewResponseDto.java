package nbcamp.TwoFastDelivery.domain.review.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@Getter
@AllArgsConstructor
public class UpdateReviewResponseDto {
    private UUID reviewId;
    private Integer rating;
    private String content;
}
