package nbcamp.TwoFastDelivery.address.presentation;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import nbcamp.TwoFastDelivery.address.application.ChangeAddressService;
import nbcamp.TwoFastDelivery.address.application.CreateAddressService;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/v1/addresses")
@RequiredArgsConstructor
public class AddressController {

    private final CreateAddressService createAddressService;
    private final ChangeAddressService changeAddressService;

    @PostMapping
    public AddressResponseDto.CreateResult createAddress(@RequestBody @Valid AddressRequestDto.Create request) {
        UUID addressId = createAddressService.createAddress(request.toServiceDto());
        return new AddressResponseDto.CreateResult(addressId);
    }

    @PatchMapping("/{addressId}")
    public void updateAddress(
            @PathVariable UUID addressId,
            @RequestBody @Valid AddressRequestDto.Change request) {
        changeAddressService.changeAddress(request.toServiceDto(addressId));
    }

    @DeleteMapping("/{addressId}")
    public void deleteAddress(
            @PathVariable UUID addressId) {
        // TODO: userId 검증 후 삭제 로직
        changeAddressService.deleteAddress(UUID.randomUUID(), addressId);
    }

}
