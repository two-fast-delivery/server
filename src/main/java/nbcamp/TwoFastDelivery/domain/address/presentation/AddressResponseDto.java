package nbcamp.TwoFastDelivery.domain.address.presentation;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

public class AddressResponseDto {

    @Getter @Builder @AllArgsConstructor
    public static class CreateResult {
        private UUID addressId;
    }
}
