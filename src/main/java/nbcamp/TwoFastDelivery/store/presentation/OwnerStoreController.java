package nbcamp.TwoFastDelivery.store.presentation;

import java.util.Set;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import nbcamp.TwoFastDelivery.global.common.CommonResponse;
import nbcamp.TwoFastDelivery.store.application.CurrentUser;
import nbcamp.TwoFastDelivery.store.application.StoreService;
import nbcamp.TwoFastDelivery.store.application.dto.StoreUpdateRequest;

@RestController
@RequestMapping("/owner/stores")
@RequiredArgsConstructor
public class OwnerStoreController {
    
    private final StoreService storeService;


    // User-Id 변경 필요
    @PatchMapping("/{id}")
    public ResponseEntity<CommonResponse<Void>> update(
        @PathVariable UUID id, @RequestBody @Valid StoreUpdateRequest request, @RequestHeader(value = "User-Id", required = false) UUID userId){
        CurrentUser user = new CurrentUser(userId, Set.of("OWNER"));
        storeService.updateStore(id, request, user);

        return ResponseEntity.ok(CommonResponse.success("가게 정보가 수정되었습니다", null));
    }
}
