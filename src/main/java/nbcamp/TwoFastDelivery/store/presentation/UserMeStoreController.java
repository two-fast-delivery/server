package nbcamp.TwoFastDelivery.store.presentation;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import nbcamp.TwoFastDelivery.global.common.CommonResponse;
import nbcamp.TwoFastDelivery.store.application.CurrentUser;
import nbcamp.TwoFastDelivery.store.application.StoreService;
import nbcamp.TwoFastDelivery.store.application.dto.StoreSummaryResponse;

@RestController
@RequestMapping("/users/me")
@RequiredArgsConstructor
public class UserMeStoreController {

    private final StoreService storeService;

    @GetMapping("/stores")
    public ResponseEntity<CommonResponse<List<StoreSummaryResponse>>> getMyStores(@RequestHeader(value = "User-Id",required = false)UUID userId){
        CurrentUser user = new CurrentUser(userId, Set.of("OWNER"));
        List<StoreSummaryResponse> result = storeService.getMyStores(user);

        return ResponseEntity.ok(CommonResponse.success("내 가게 목록 조회 성공", result));
    }
    
}
