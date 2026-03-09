package nbcamp.TwoFastDelivery.domain.address.application;

import lombok.RequiredArgsConstructor;
import nbcamp.TwoFastDelivery.domain.address.application.dto.AddressServiceDto;
import nbcamp.TwoFastDelivery.domain.address.domain.Address;
import nbcamp.TwoFastDelivery.domain.address.domain.AddressRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CreateAddressService {

    private final AddressRepository addressRepository;

    @Transactional
    public UUID createAddress(AddressServiceDto.Create dto) {
        Address address = Address.builder()
                .alias(dto.getAlias())
                .address(dto.getAddress())
                .detailAddress(dto.getDetailAddress())
                .build();

        return addressRepository.save(address).getAddressId();
    }
}
