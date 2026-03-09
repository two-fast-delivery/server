package nbcamp.TwoFastDelivery.domain.store.presentation;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.data.domain.Sort;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import nbcamp.TwoFastDelivery.domain.store.application.CurrentUser;
import nbcamp.TwoFastDelivery.domain.store.application.StoreService;
import nbcamp.TwoFastDelivery.domain.store.application.dto.StoreCreateRequest;
import nbcamp.TwoFastDelivery.domain.store.application.dto.StoreDetailResponse;
import nbcamp.TwoFastDelivery.domain.store.application.dto.StoreSearchCondition;
import nbcamp.TwoFastDelivery.domain.store.application.dto.StoreSummaryResponse;
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
    
    public ResponseEntity<CommonResponse<Page<StoreSummaryResponse>>> getStores(
        @RequestParam(required = false)UUID categoryId,
        @RequestParam(required = false)String keyword,
        @RequestParam(defaultValue = "0")int page,
        @RequestParam(defaultValue = "0")int size,
        @RequestParam(defaultValue = "createdAt,desc")String sort,
        @RequestParam(value = "User-Id", required = false)UUID userId
    ){
        CurrentUser user = new CurrentUser(userId, Set.of("CUSTOMER"));
        StoreSearchCondition condition = new StoreSearchCondition();
        // TODO: 정렬 조건 설정 필요
        
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());

        Page<StoreSummaryResponse> result = storeService.getStores(condition, pageable, user);

        return ResponseEntity.ok(CommonResponse.success("가게 목록 조회 성공", result));
    }

    public ResponseEntity<CommonResponse<List<StoreSummaryResponse>>> getMyStores(
        @RequestHeader(value = "User-Id", required = false)UUID userId
    ){
        CurrentUser user = new CurrentUser(userId, Set.of("OWNER"));
        List<StoreSummaryResponse> result = storeService.getMyStores(user);

        return ResponseEntity.ok(CommonResponse.success("내 가게 목록 조회 성공", result));
    }
}
