package nbcamp.TwoFastDelivery.domain.store.presentation;

import java.util.Set;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import nbcamp.TwoFastDelivery.domain.store.application.CurrentUser;
import nbcamp.TwoFastDelivery.domain.store.application.StoreService;
import nbcamp.TwoFastDelivery.domain.store.application.dto.StoreCreateRequest;
import nbcamp.TwoFastDelivery.domain.store.application.dto.StoreDetailResponse;
import nbcamp.TwoFastDelivery.global.common.CommonResponse;


@RestController
@RequestMapping("/stores")
@RequiredArgsConstructor
public class StoreController {

    private final StoreService storeService;

    @PostMapping
    public ResponseEntity<CommonResponse<UUID>> create(
        @RequestBody 
        @Valid 
        StoreCreateRequest request,
        @RequestHeader(value = "User-Id", required = false)
        UUID userId){
        /*로그인 구현보고 수정 */
        CurrentUser user = new CurrentUser(userId, Set.of("CUSTOMER"));
        UUID storeId = storeService.createStore(request, user);
        return ResponseEntity.status(HttpStatus.CREATED).body(CommonResponse.success("가게 등록 요청이 접수되었습니다.", storeId));
    }


    @GetMapping("/{id}")
    public ResponseEntity<CommonResponse<StoreDetailResponse>> get(
        @PathVariable UUID id,
        @RequestHeader(value = "User-Id", required = false)
        UUID userId){

        CurrentUser user = new CurrentUser(userId, Set.of("CUSTOMER"));
        StoreDetailResponse res = storeService.getStore(id, user);
        return ResponseEntity.ok(CommonResponse.success("가게 상세 조회 성공", res));
    }
}
