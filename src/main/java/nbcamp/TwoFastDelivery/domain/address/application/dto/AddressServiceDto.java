package nbcamp.TwoFastDelivery.domain.address.application.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AddressServiceDto {

    @Getter
    @Builder
    public static class Create {
        private String alias;
        private String address;
        private String detailAddress;
    }
}
