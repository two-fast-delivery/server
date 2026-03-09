package nbcamp.TwoFastDelivery.domain.address.presentation;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import nbcamp.TwoFastDelivery.domain.address.application.CreateAddressService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/v1/addresses")
@RequiredArgsConstructor
public class AddressController {

    private final CreateAddressService createAddressService;

    @PostMapping
    public AddressResponseDto.CreateResult createAddress(@RequestBody @Valid AddressRequestDto.Create request) {
        UUID addressId = createAddressService.createAddress(request.toServiceDto());
        return new AddressResponseDto.CreateResult(addressId);
    }
}
