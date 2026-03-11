package nbcamp.TwoFastDelivery.domain.store.presentation;

import java.util.Set;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import nbcamp.TwoFastDelivery.domain.store.application.CurrentUser;
import nbcamp.TwoFastDelivery.domain.store.application.StoreRequestService;
import nbcamp.TwoFastDelivery.domain.store.application.dto.StoreRequestDecisionRequest;
import nbcamp.TwoFastDelivery.global.common.CommonResponse;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/store-requests")
public class AdminStoreRequestController {
    
    private final StoreRequestService storeRequestService;

    public ResponseEntity<CommonResponse<Void>> decide(UUID id, StoreRequestDecisionRequest request, UUID userId){
        CurrentUser admin = new CurrentUser(userId, Set.of("MASTER")); //이후 수정
        storeRequestService.decideRequest(id, request, admin);

        return ResponseEntity.ok(CommonResponse.success("가게 요청이 처리되었습니다.", null));
    }
}
