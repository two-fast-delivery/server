package nbcamp.TwoFastDelivery.domain.review.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@Getter
@AllArgsConstructor
public class DetailReviewRquestDto {
    @NotNull
    private UUID reviewId;
}
