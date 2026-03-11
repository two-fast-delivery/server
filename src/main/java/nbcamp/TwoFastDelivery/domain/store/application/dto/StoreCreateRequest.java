package nbcamp.TwoFastDelivery.domain.store.application.dto;

import java.util.UUID;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
public class StoreCreateRequest {

    @NotBlank(message = "가게명은 필수입니다.")
    private String name;

    @NotBlank(message = "주소는 필수입니다.")
    private String address;

    @NotBlank(message = "전화번호는 필수입니다.")
    private String phone;

    @NotNull(message = "카테고리는 필수입니다.")
    private UUID categoryId;

    private String thumbnailUrl;

    @NotBlank(message = "영업 시작 시간은 필수입니다.")
    private String openTime;

    @NotBlank(message = "영업 종료 시간은 필수입니다.")
    private String closeTime;

    // 가게 설명
    private String description;

    // 관리자에게 보여줄 가게 설명
    private String storeDesc;
}
