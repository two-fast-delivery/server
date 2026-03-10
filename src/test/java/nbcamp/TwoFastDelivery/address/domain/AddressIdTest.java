package nbcamp.TwoFastDelivery.address.domain;

import nbcamp.TwoFastDelivery.address.domain.AddressId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.util.UUID;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("AddressId 단위 테스트")
class AddressIdTest {

    @Test
    @DisplayName("성공: 새로운 AddressId 생성 시 UUID 자동 할당")
    void createNewId() {
        // given & when
        AddressId id = AddressId.of();

        // then
        assertThat(id.getId()).isNotNull();
    }

    @Test
    @DisplayName("성공: 특정 UUID 입력 시 해당 값으로 AddressId 생성")
    void createWithExistingId() {
        // given
        UUID uuid = UUID.randomUUID();

        // when
        AddressId id = AddressId.of(uuid);

        // then
        assertThat(id.getId()).isEqualTo(uuid);
    }
}
