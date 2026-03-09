package nbcamp.TwoFastDelivery.address.application;

import lombok.RequiredArgsConstructor;
import nbcamp.TwoFastDelivery.address.application.dto.AddressServiceDto;
import nbcamp.TwoFastDelivery.address.domain.Address;
import nbcamp.TwoFastDelivery.address.domain.AddressId;
import nbcamp.TwoFastDelivery.address.domain.AddressRepository;
import nbcamp.TwoFastDelivery.global.exception.CustomException;
import nbcamp.TwoFastDelivery.global.exception.ErrorCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ChangeAddressService {

    private final AddressRepository addressRepository;

    @Transactional
    public void changeAddress(AddressServiceDto.Change dto) {
        Address address = getAddress(AddressId.of(dto.getAddressId()));

        address.change(
                dto.getAlias(),
                dto.getAddress(),
                dto.getDetailAddress()
        );
    }

    private Address getAddress(AddressId addressId) {
        Address address = addressRepository.findById(addressId)
                .orElseThrow(() -> new CustomException(ErrorCode.ADDRESS_NOT_FOUND));
        return address;
    }
}
