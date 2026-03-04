package nbcamp.TwoFastDelivery.domain.address.domain;

import jakarta.persistence.*;
import lombok.*;
import nbcamp.TwoFastDelivery.global.common.BaseEntity;
import org.springframework.util.Assert;

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
    public Address(UUID addressId, String alias, String address, String detailAddress) {
        checkValidAddress(alias, address, detailAddress);

        this.id = initializedId(addressId);
        this.alias = alias;
        this.address = address;
        this.detailAddress = detailAddress;
    }

    public void update(String alias, String address, String detailAddress) {
        checkValidAddress(alias, address, detailAddress);
        this.alias = alias;
        this.address = address;
        this.detailAddress = detailAddress;
    }

    private AddressId initializedId(UUID addressId) {
        if (addressId == null) {
            return AddressId.of();
        }

        return AddressId.of(addressId);
    }

    private void checkValidAddress(String alias, String address, String detailAddress) {
        Assert.hasText(alias, "배송지 별칭은 필수입니다.");
        Assert.hasText(address, "기본 주소는 필수입니다.");
        Assert.hasText(detailAddress, "상세 주소는 필수입니다.");

        Assert.isTrue(alias.length() <= 50, "별칭은 50자 이내여야 합니다.");
        Assert.isTrue(address.length() <= 255, "주소는 255자 이내여야 합니다.");
        Assert.isTrue(detailAddress.length() <= 255, "상세 주소는 255자 이내여야 합니다.");
    }
}