package nbcamp.TwoFastDelivery.store.application.dto;

import java.util.UUID;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class StoreSearchCondition {
    
    private UUID categoryId;
    private String keyword;
    private String sort;
}
