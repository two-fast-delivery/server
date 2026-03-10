package nbcamp.TwoFastDelivery.domain.reviewReport.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@Getter
@AllArgsConstructor
public class ReviewReportCreateResponseDto {
    private UUID reportId;
    private UUID reviewId;
}
