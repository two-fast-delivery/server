package nbcamp.TwoFastDelivery.address.domain;

import jakarta.persistence.*;
import lombok.*;
import nbcamp.TwoFastDelivery.global.common.BaseEntity;
import nbcamp.TwoFastDelivery.global.exception.CustomException;
import nbcamp.TwoFastDelivery.global.exception.ErrorCode;
import org.springframework.util.StringUtils;

import java.util.UUID;

@Entity
@ToString @Getter
@Table(name = "P_ADDRESS")
@Access(AccessType.FIELD)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Address extends BaseEntity {

    @EmbeddedId
    private AddressId id;

    @Column(nullable = false, length = 50)
    private String alias;

    @Column(nullable = false, length = 255)
    private String address;

    @Column(nullable = false, length = 255)
    private String detailAddress;

    @Builder
    private Address(UUID addressId, String alias, String address, String detailAddress) {
        checkValidAddress(alias, address, detailAddress);

        this.id = initializedId(addressId);
        this.alias = alias;
        this.address = address;
        this.detailAddress = detailAddress;
    }

    public void change(String alias, String address, String detailAddress) {
        checkValidAddress(alias, address, detailAddress);
        this.alias = alias;
        this.address = address;
        this.detailAddress = detailAddress;
    }

    public UUID getAddressId() {
        return this.id != null ? this.id.getId() : null;
    }

    private AddressId initializedId(UUID addressId) {
        if (addressId == null) {
            return AddressId.of();
        }

        return AddressId.of(addressId);
    }

    private void checkValidAddress(String alias, String address, String detailAddress) {
        if (!StringUtils.hasText(alias)) {
            throw new CustomException(ErrorCode.ADDRESS_ALIAS_REQUIRED);
        }
        if (!StringUtils.hasText(address)) {
            throw new CustomException(ErrorCode.ADDRESS_VALUE_REQUIRED);
        }
        if (!StringUtils.hasText(detailAddress)) {
            throw new CustomException(ErrorCode.DETAIL_ADDRESS_REQUIRED);
        }

        if (alias.length() > 50) {
            throw new CustomException(ErrorCode.ADDRESS_ALIAS_TOO_LONG);
        }
        if (address.length() > 255) {
            throw new CustomException(ErrorCode.ADDRESS_VALUE_TOO_LONG);
        }
        if (detailAddress.length() > 255) {
            throw new CustomException(ErrorCode.DETAIL_ADDRESS_TOO_LONG);
        }
    }

}