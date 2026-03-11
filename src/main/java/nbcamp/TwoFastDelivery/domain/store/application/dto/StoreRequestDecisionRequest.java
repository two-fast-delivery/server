package nbcamp.TwoFastDelivery.domain.store.application.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.NotNull;

@Getter
@NoArgsConstructor
public class StoreRequestDecisionRequest {

    @NotNull(message = "승인 여부는 필수입니다")
    private Boolean approved; // true/false 승인/거절
    
    private String reason; // 승인 거절 사유
}
