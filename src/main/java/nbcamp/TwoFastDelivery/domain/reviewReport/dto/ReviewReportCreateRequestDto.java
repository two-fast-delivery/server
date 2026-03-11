package nbcamp.TwoFastDelivery.domain.reviewReport.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor  //-> 추후 다른 repuestDto에도 붙여야 함
public class ReviewReportCreateRequestDto {
    @NotNull
    private String reportContent;
}
