package nbcamp.TwoFastDelivery.domain.address.presentation;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import nbcamp.TwoFastDelivery.domain.address.application.dto.AddressServiceDto;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AddressRequestDto {

    @Getter @Setter @Builder @NoArgsConstructor @AllArgsConstructor
    public static class Create {

        @NotBlank(message = "주소 별칭은 필수 입력 값입니다.")
        @Size(max = 50, message = "별칭은 50자 이내로 입력해주세요.")
        private String alias;

        @NotBlank(message = "주소는 필수 입력 값입니다.")
        @Size(max = 255, message = "주소는 255자 이내로 입력해주세요.")
        private String address;

        @NotBlank(message = "상세 주소는 필수 입력 값입니다.")
        @Size(max = 255, message = "상세 주소는 255자 이내로 입력해주세요.")
        @JsonProperty("detail_address")
        private String detailAddress;

        public AddressServiceDto.Create toServiceDto() {
            return AddressServiceDto.Create.builder()
                    .alias(alias)
                    .address(address)
                    .detailAddress(detailAddress)
                    .build();
        }
    }
}