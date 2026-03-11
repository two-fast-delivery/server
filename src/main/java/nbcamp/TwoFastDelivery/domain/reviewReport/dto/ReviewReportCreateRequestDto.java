package nbcamp.TwoFastDelivery.domain.reviewReport.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ReviewReportCreateRequestDto {
    @NotNull
    private String reportContent;
}
