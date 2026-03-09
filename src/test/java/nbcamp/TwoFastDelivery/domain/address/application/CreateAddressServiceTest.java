package nbcamp.TwoFastDelivery.domain.address.application;

import nbcamp.TwoFastDelivery.domain.address.application.dto.AddressServiceDto;
import nbcamp.TwoFastDelivery.domain.address.domain.Address;
import nbcamp.TwoFastDelivery.domain.address.domain.AddressRepository;
import nbcamp.TwoFastDelivery.global.exception.CustomException;
import nbcamp.TwoFastDelivery.global.exception.ErrorCode;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@DisplayName("CreateAddressService 단위 테스트")
class CreateAddressServiceTest {

    private CreateAddressService createAddressService;

    @Mock
    private AddressRepository addressRepository;

    private AutoCloseable closeable;

    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
        createAddressService = new CreateAddressService(addressRepository);
    }

    @AfterEach
    void tearDown() throws Exception {
        closeable.close();
    }

    @Test
    @DisplayName("성공: 유효성 검증 성공하는 DTO로 입력 시 정상적으로 저장")
    void createAddress_Success() {
        AddressServiceDto.Create dto = AddressServiceDto.Create.builder()
                .alias("우리집")
                .address("서울시 강남구")
                .detailAddress("101호")
                .build();

        Address savedAddress = Address.builder()
                .alias(dto.getAlias())
                .address(dto.getAddress())
                .detailAddress(dto.getDetailAddress())
                .build();

        given(addressRepository.save(any(Address.class))).willReturn(savedAddress);

        UUID resultId = createAddressService.createAddress(dto);

        assertThat(resultId).isEqualTo(savedAddress.getAddressId());
    }

    @Nested
    @DisplayName("실패: 유효성 검증")
    class ValidationFailTest {

        @ParameterizedTest
        @NullAndEmptySource
        @DisplayName("실패: 별칭 누락 시 예외 발생")
        void aliasMandatoryFail(String invalidAlias) {
            AddressServiceDto.Create dto = AddressServiceDto.Create.builder()
                    .alias(invalidAlias)
                    .address("주소")
                    .detailAddress("상세 주소")
                    .build();

            assertThatThrownBy(() -> createAddressService.createAddress(dto))
                    .isInstanceOf(CustomException.class)
                    .hasFieldOrPropertyWithValue("errorCode", ErrorCode.ADDRESS_ALIAS_REQUIRED);
        }

        @Test
        @DisplayName("실패: 별칭 50자 초과 시 예외 발생")
        void aliasLengthFail() {
            AddressServiceDto.Create dto = AddressServiceDto.Create.builder()
                    .alias("가".repeat(51))
                    .address("주소")
                    .detailAddress("상세 주소")
                    .build();

            assertThatThrownBy(() -> createAddressService.createAddress(dto))
                    .isInstanceOf(CustomException.class)
                    .hasFieldOrPropertyWithValue("errorCode", ErrorCode.ADDRESS_ALIAS_TOO_LONG);
        }

        @ParameterizedTest
        @NullAndEmptySource
        @DisplayName("실패: 기본 주소 누락 시 예외 발생")
        void addressMandatoryFail(String invalidAddress) {
            AddressServiceDto.Create dto = AddressServiceDto.Create.builder()
                    .alias("별칭")
                    .address(invalidAddress)
                    .detailAddress("상세 주소")
                    .build();

            assertThatThrownBy(() -> createAddressService.createAddress(dto))
                    .isInstanceOf(CustomException.class)
                    .hasFieldOrPropertyWithValue("errorCode", ErrorCode.ADDRESS_VALUE_REQUIRED);
        }

        @Test
        @DisplayName("실패: 기본 주소 255자 초과 시 예외 발생")
        void addressLengthFail() {
            AddressServiceDto.Create dto = AddressServiceDto.Create.builder()
                    .alias("별칭")
                    .address("가".repeat(256))
                    .detailAddress("상세 주소")
                    .build();

            assertThatThrownBy(() -> createAddressService.createAddress(dto))
                    .isInstanceOf(CustomException.class)
                    .hasFieldOrPropertyWithValue("errorCode", ErrorCode.ADDRESS_VALUE_TOO_LONG);
        }

        @ParameterizedTest
        @NullAndEmptySource
        @DisplayName("실패: 상세 주소 누락 시 예외 발생")
        void detailAddressMandatoryFail(String invalidDetail) {
            AddressServiceDto.Create dto = AddressServiceDto.Create.builder()
                    .alias("별칭")
                    .address("주소")
                    .detailAddress(invalidDetail)
                    .build();

            assertThatThrownBy(() -> createAddressService.createAddress(dto))
                    .isInstanceOf(CustomException.class)
                    .hasFieldOrPropertyWithValue("errorCode", ErrorCode.DETAIL_ADDRESS_REQUIRED);
        }

        @Test
        @DisplayName("실패: 상세 주소 255자 초과 시 예외 발생")
        void detailAddressLengthFail() {
            AddressServiceDto.Create dto = AddressServiceDto.Create.builder()
                    .alias("별칭")
                    .address("주소")
                    .detailAddress("가".repeat(256))
                    .build();

            assertThatThrownBy(() -> createAddressService.createAddress(dto))
                    .isInstanceOf(CustomException.class)
                    .hasFieldOrPropertyWithValue("errorCode", ErrorCode.DETAIL_ADDRESS_TOO_LONG);
        }
    }
}
