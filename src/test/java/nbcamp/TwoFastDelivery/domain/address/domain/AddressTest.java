package nbcamp.TwoFastDelivery.domain.address.domain;

import nbcamp.TwoFastDelivery.global.exception.CustomException;
import nbcamp.TwoFastDelivery.global.exception.ErrorCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("Address 도메인 단위 테스트")
class AddressTest {

    private Address createDefaultAddress() {
        return createAddress("우리집", "서울시 강남구", "101호");
    }

    private Address createAddress(String alias, String address, String detailAddress) {
        return Address.builder()
                .alias(alias)
                .address(address)
                .detailAddress(detailAddress)
                .build();
    }

    @Nested
    @DisplayName("Address 생성 검증")
    class CreationTest {

        @Test
        @DisplayName("성공: 모든 정보가 올바르면 address 객체 생성")
        void createSuccess() {
            String alias = "우리집";
            String addressName = "서울시 강남구";
            String detailAddress = "101호";
            Address address = createAddress(alias, addressName, detailAddress);

            assertThat(address).isNotNull();
            assertThat(address.getAddressId()).isNotNull();
            assertThat(address.getAlias()).isEqualTo(alias);
            assertThat(address.getAddress()).isEqualTo(addressName);
            assertThat(address.getDetailAddress()).isEqualTo(detailAddress);
        }

        @ParameterizedTest
        @NullAndEmptySource
        @ValueSource(strings = {" ", "  "})
        @DisplayName("실패: 생성 시 별칭이 비어있으면 예외 발생")
        void aliasMandatoryFail(String invalidValue) {
            assertThatThrownBy(() -> createAddress(invalidValue, "주소", "상세"))
                    .isInstanceOf(CustomException.class)
                    .hasFieldOrPropertyWithValue("errorCode", ErrorCode.ADDRESS_ALIAS_REQUIRED);
        }

        @ParameterizedTest
        @NullAndEmptySource
        @ValueSource(strings = {" ", "  "})
        @DisplayName("실패: 생성 시 주소가 비어있으면 예외 발생")
        void addressMandatoryFail(String invalidValue) {
            assertThatThrownBy(() -> createAddress("별칭", invalidValue, "상세"))
                    .isInstanceOf(CustomException.class)
                    .hasFieldOrPropertyWithValue("errorCode", ErrorCode.ADDRESS_VALUE_REQUIRED);
        }

        @ParameterizedTest
        @NullAndEmptySource
        @ValueSource(strings = {" ", "  "})
        @DisplayName("실패: 생성 시 상세 주소가 비어있으면 예외 발생")
        void detailAddressMandatoryFail(String invalidValue) {
            assertThatThrownBy(() -> createAddress("별칭", "주소", invalidValue))
                    .isInstanceOf(CustomException.class)
                    .hasFieldOrPropertyWithValue("errorCode", ErrorCode.DETAIL_ADDRESS_REQUIRED);
        }

        @Test
        @DisplayName("실패: 생성 시 별칭이 50자를 초과하면 예외 발생")
        void aliasLengthFail() {
            String longAlias = "가".repeat(51);
            assertThatThrownBy(() -> createAddress(longAlias, "주소", "상세"))
                    .isInstanceOf(CustomException.class)
                    .hasFieldOrPropertyWithValue("errorCode", ErrorCode.ADDRESS_ALIAS_TOO_LONG);
        }

        @Test
        @DisplayName("실패: 생성 시 주소가 255자를 초과하면 예외 발생")
        void addressLengthFail() {
            String longText = "가".repeat(256);
            assertThatThrownBy(() -> createAddress("별칭", longText, "상세"))
                    .isInstanceOf(CustomException.class)
                    .hasFieldOrPropertyWithValue("errorCode", ErrorCode.ADDRESS_VALUE_TOO_LONG);
        }

        @Test
        @DisplayName("실패: 생성 시 상세 주소가 255자를 초과하면 예외 발생")
        void detailAddressLengthFail() {
            String longText = "가".repeat(256);
            assertThatThrownBy(() -> createAddress("별칭", "주소", longText))
                    .isInstanceOf(CustomException.class)
                    .hasFieldOrPropertyWithValue("errorCode", ErrorCode.DETAIL_ADDRESS_TOO_LONG);
        }
    }

    @Nested
    @DisplayName("Address 수정 검증")
    class UpdateTest {

        @Test
        @DisplayName("성공: 유효한 정보로 수정 시 필드 값 변경")
        void updateSuccess() {
            Address address = createDefaultAddress();
            address.update("회사", "성남시 분당구", "202호");

            assertThat(address.getAlias()).isEqualTo("회사");
            assertThat(address.getAddress()).isEqualTo("성남시 분당구");
            assertThat(address.getDetailAddress()).isEqualTo("202호");
        }

        @ParameterizedTest
        @NullAndEmptySource
        @ValueSource(strings = {" ", "  "})
        @DisplayName("실패: 수정 시 별칭이 비어있으면 예외 발생")
        void updateAliasMandatoryFail(String invalidValue) {
            Address address = createDefaultAddress();
            assertThatThrownBy(() -> address.update(invalidValue, "주소", "상세"))
                    .isInstanceOf(CustomException.class)
                    .hasFieldOrPropertyWithValue("errorCode", ErrorCode.ADDRESS_ALIAS_REQUIRED);
        }

        @ParameterizedTest
        @NullAndEmptySource
        @ValueSource(strings = {" ", "  "})
        @DisplayName("실패: 수정 시 주소가 비어있으면 예외 발생")
        void updateAddressMandatoryFail(String invalidValue) {
            Address address = createDefaultAddress();
            assertThatThrownBy(() -> address.update("별칭", invalidValue, "상세"))
                    .isInstanceOf(CustomException.class)
                    .hasFieldOrPropertyWithValue("errorCode", ErrorCode.ADDRESS_VALUE_REQUIRED);
        }

        @ParameterizedTest
        @NullAndEmptySource
        @ValueSource(strings = {" ", "  "})
        @DisplayName("실패: 수정 시 상세 주소가 비어있으면 예외 발생")
        void updateDetailAddressMandatoryFail(String invalidValue) {
            Address address = createDefaultAddress();
            assertThatThrownBy(() -> address.update("별칭", "주소", invalidValue))
                    .isInstanceOf(CustomException.class)
                    .hasFieldOrPropertyWithValue("errorCode", ErrorCode.DETAIL_ADDRESS_REQUIRED);
        }

        @Test
        @DisplayName("실패: 수정 시 별칭이 50자를 초과하면 예외 발생")
        void updateAliasLengthFail() {
            Address address = createDefaultAddress();
            String longAlias = "가".repeat(51);
            assertThatThrownBy(() -> address.update(longAlias, "주소", "상세"))
                    .isInstanceOf(CustomException.class)
                    .hasFieldOrPropertyWithValue("errorCode", ErrorCode.ADDRESS_ALIAS_TOO_LONG);
        }

        @Test
        @DisplayName("실패: 수정 시 주소가 255자를 초과하면 예외 발생")
        void updateAddressLengthFail() {
            Address address = createDefaultAddress();
            String longText = "가".repeat(256);
            assertThatThrownBy(() -> address.update("별칭", longText, "상세"))
                    .isInstanceOf(CustomException.class)
                    .hasFieldOrPropertyWithValue("errorCode", ErrorCode.ADDRESS_VALUE_TOO_LONG);
        }

        @Test
        @DisplayName("실패: 수정 시 상세 주소가 255자를 초과하면 예외 발생")
        void updateDetailAddressLengthFail() {
            Address address = createDefaultAddress();
            String longText = "가".repeat(256);
            assertThatThrownBy(() -> address.update("별칭", "주소", longText))
                    .isInstanceOf(CustomException.class)
                    .hasFieldOrPropertyWithValue("errorCode", ErrorCode.DETAIL_ADDRESS_TOO_LONG);
        }
    }
}
