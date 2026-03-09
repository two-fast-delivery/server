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

        @NotBlank(message = "{address.alias.required}")
        @Size(max = 50, message = "{address.alias.too_long}")
        private String alias;

        @NotBlank(message = "{address.address.required}")
        @Size(max = 255, message = "{address.address.too_long}")
        private String address;

        @NotBlank(message = "{address.detail.required}")
        @Size(max = 255, message = "{address.detail.too_long}")
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