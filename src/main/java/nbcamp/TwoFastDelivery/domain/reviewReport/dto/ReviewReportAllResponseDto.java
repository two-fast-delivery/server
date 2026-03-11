package nbcamp.TwoFastDelivery.domain.reviewReport.dto;
import lombok.Builder;
import lombok.Getter;
import nbcamp.TwoFastDelivery.domain.reviewReport.entity.ReviewReport;
import nbcamp.TwoFastDelivery.domain.reviewReport.enums.ReviewReportStatus;

import java.util.UUID;

@Getter
@Builder
public class ReviewReportAllResponseDto {
    private UUID reportId;
    private UUID reviewId;
    private UUID userId;
    private String content;
    private ReviewReportStatus status;

    public static ReviewReportAllResponseDto from(ReviewReport report) {
        return ReviewReportAllResponseDto.builder()
                .reportId(report.getId())
                .reviewId(report.getReview().getId())
                .userId(report.getReview().getUserId())
                .status(report.getStatus())
                .content(report.getContent())
                .build();
    }
}
