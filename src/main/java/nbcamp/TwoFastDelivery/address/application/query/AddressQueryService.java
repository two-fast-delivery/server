package nbcamp.TwoFastDelivery.address.application.query;

import lombok.RequiredArgsConstructor;
import nbcamp.TwoFastDelivery.address.domain.Address;
import nbcamp.TwoFastDelivery.address.domain.AddressId;
import nbcamp.TwoFastDelivery.address.domain.AddressRepository;
import nbcamp.TwoFastDelivery.address.presentation.AddressResponseDto;
import nbcamp.TwoFastDelivery.global.exception.CustomException;
import nbcamp.TwoFastDelivery.global.exception.ErrorCode;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AddressQueryService {

    private final AddressRepository addressRepository;

    public AddressResponseDto.Address getAddressById(UUID userId, UUID addressId) {
        Address address = getAddress(AddressId.of(addressId));
        address.validateOwner(userId);

        return toAddressResponse(address);
    }

    public Page<AddressResponseDto.Address> getUserAddresses(UUID uuid, Pageable pageable) {
        return addressRepository.findAllByCreatedBy(uuid, pageable)
                .map(this::toAddressResponse);
    }

    private Address getAddress(AddressId addressId) {
        return addressRepository.findById(addressId)
                .orElseThrow(() -> new CustomException(ErrorCode.ADDRESS_NOT_FOUND));
    }

    private AddressResponseDto.Address toAddressResponse(Address address) {
        return AddressResponseDto.Address.builder()
                .addressId(address.getAddressId())
                .alias(address.getAlias())
                .address(address.getAddress())
                .detailAddress(address.getDetailAddress())
                .build();
    }

}
