package nbcamp.TwoFastDelivery.store.application.dto;

import java.util.UUID;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class StoreUpdateRequest {
    private String name;
    private String address;
    private String phone;
    private UUID categoryId;
    private String thumbnailUrl;
    private String openTime;
    private String closeTime;
    private String description;
}
/* null이면 기존값 유지 */