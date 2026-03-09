package nbcamp.TwoFastDelivery.domain.address.application.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AddressServiceDto {

    @Getter
    @Builder
    public static class Create {
        private String alias;
        private String address;
        private String detailAddress;
    }

    @Getter
    @Builder
    public static class Change {
        private UUID addressId;
        private String alias;
        private String address;
        private String detailAddress;
    }
}
