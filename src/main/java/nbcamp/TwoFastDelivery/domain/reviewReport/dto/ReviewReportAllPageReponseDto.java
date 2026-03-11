package nbcamp.TwoFastDelivery.domain.reviewReport.dto;

import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
@Builder
public class ReviewReportAllPageReponseDto {
    private int page;
    private int totalPages;
    private int size;
    private long totalElements;
    private List<ReviewReportAllResponseDto> reports;

    public static ReviewReportAllPageReponseDto of(
            Page<?> reportPage,
            List<ReviewReportAllResponseDto> reports
    ) {
        return ReviewReportAllPageReponseDto.builder()
                .page(reportPage.getNumber())
                .totalPages(reportPage.getTotalPages())
                .size(reportPage.getSize())
                .totalElements(reportPage.getTotalElements())
                .reports(reports)
                .build();
    }
}
