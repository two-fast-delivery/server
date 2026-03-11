package nbcamp.TwoFastDelivery.store.presentation;

import java.util.Set;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import nbcamp.TwoFastDelivery.global.common.CommonResponse;
import nbcamp.TwoFastDelivery.store.application.CurrentUser;
import nbcamp.TwoFastDelivery.store.application.StoreRequestService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/owner/store-requests")
public class OwnerStoreRequestController {
    
    private final StoreRequestService storeRequestService;

    @PostMapping("/{id}")
    public ResponseEntity<CommonResponse<Void>> requestDelete(@PathVariable UUID id, @RequestHeader(value = "User-Id", required = false)UUID UserId){
        CurrentUser user = new CurrentUser(UserId, Set.of("OWNER"));
        storeRequestService.requestDeleteStore(id, user);

        return ResponseEntity.status(HttpStatus.CREATED).body(CommonResponse.success("가게 삭제 요청이 접수되었습니다" ,null));
    }
}
