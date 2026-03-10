package nbcamp.TwoFastDelivery.store.presentation;

import java.util.Set;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import nbcamp.TwoFastDelivery.global.common.CommonResponse;
import nbcamp.TwoFastDelivery.store.application.CurrentUser;
import nbcamp.TwoFastDelivery.store.application.StoreRequestService;
import nbcamp.TwoFastDelivery.store.application.dto.StoreRequestDecisionRequest;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/store-requests")
public class AdminStoreRequestController {
    
    private final StoreRequestService storeRequestService;

    @PatchMapping("/{id}")
    public ResponseEntity<CommonResponse<Void>> decide(
        @PathVariable("id")UUID id, @RequestBody @Valid StoreRequestDecisionRequest request, @RequestHeader(value = "User-Id", required = false)UUID userId){
        CurrentUser admin = new CurrentUser(userId, Set.of("MASTER")); //이후 수정
        storeRequestService.decideRequest(id, request, admin);

        return ResponseEntity.ok(CommonResponse.success("가게 요청이 처리되었습니다.", null));
    }
}
