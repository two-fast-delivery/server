package nbcamp.TwoFastDelivery.domain.reviewReport.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import nbcamp.TwoFastDelivery.domain.reviewReport.enums.ReviewReportStatus;

import java.util.UUID;

@Getter
@AllArgsConstructor
public class ReviewReportDetailResponseDto {
    private UUID reportId;
    private Long reportUserId;
    private ReviewReportStatus status;
    private UUID reviewId;
    private Integer rating;
    private String reviewContent;
    private String reportContent;
}
