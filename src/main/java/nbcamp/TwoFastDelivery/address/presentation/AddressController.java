package nbcamp.TwoFastDelivery.address.presentation;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import nbcamp.TwoFastDelivery.address.application.ChangeAddressService;
import nbcamp.TwoFastDelivery.address.application.CreateAddressService;
import nbcamp.TwoFastDelivery.address.application.query.AddressQueryService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/v1/addresses")
@RequiredArgsConstructor
public class AddressController {

    private final CreateAddressService createAddressService;
    private final ChangeAddressService changeAddressService;
    private final AddressQueryService addressQueryService;

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

    @GetMapping("/{addressId}")
    public AddressResponseDto.Address searchAddress(@PathVariable UUID addressId) {
        // TODO: userId 검증 후 조회 로직
        return addressQueryService.getAddressById(UUID.randomUUID(), addressId);
    }

    @GetMapping
    public Page<AddressResponseDto.Address> searchAddresses(Pageable pageable) {
        // TODO: userId 검증 후 조회 로직
        return addressQueryService.getUserAddresses(UUID.randomUUID(), pageable);
    }

}
