package nbcamp.TwoFastDelivery.store.presentation;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
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
import nbcamp.TwoFastDelivery.global.common.CommonResponse;
import nbcamp.TwoFastDelivery.global.exception.CustomException;
import nbcamp.TwoFastDelivery.global.exception.ErrorCode;
import nbcamp.TwoFastDelivery.store.application.CurrentUser;
import nbcamp.TwoFastDelivery.store.application.StoreService;
import nbcamp.TwoFastDelivery.store.application.dto.StoreCreateRequest;
import nbcamp.TwoFastDelivery.store.application.dto.StoreDetailResponse;
import nbcamp.TwoFastDelivery.store.application.dto.StoreSearchCondition;
import nbcamp.TwoFastDelivery.store.application.dto.StoreSummaryResponse;
import nbcamp.TwoFastDelivery.user.domain.user.User;
import nbcamp.TwoFastDelivery.user.domain.user.UserRepository;

@RestController
@RequestMapping("/stores")
@RequiredArgsConstructor
public class StoreController {

    private final StoreService storeService;
    private final UserRepository userRepository;

    private CurrentUser getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return new CurrentUser(null, Set.of());
        }
        Object principal = authentication.getPrincipal();
        if (!(principal instanceof UserDetails userDetails)) {
            return new CurrentUser(null, Set.of());
        }

        String username = userDetails.getUsername();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new CustomException(ErrorCode.UNAUTHORIZED));
        UUID userId = user.getUserId().getId();

        Set<String> roles = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .map(r -> r.replace("ROLE_", ""))
                .collect(Collectors.toSet());
        return new CurrentUser(userId, roles);
    }

    @PostMapping
    public ResponseEntity<CommonResponse<UUID>> create(
        @RequestBody
        @Valid
        StoreCreateRequest request,
        @RequestHeader(value = "User-Id", required = false)
        UUID userId
    ) {
        // TODO: JWT
        CurrentUser user = getCurrentUser(); 
        UUID storeId = storeService.createStore(request, user);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(CommonResponse.success("가게 등록 요청이 접수되었습니다.", storeId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CommonResponse<StoreDetailResponse>> get(
        @PathVariable UUID id,
        @RequestHeader(value = "User-Id", required = false)
        UUID userId
    ) {
        CurrentUser user = getCurrentUser();
        StoreDetailResponse res = storeService.getStore(id, user);
        return ResponseEntity.ok(CommonResponse.success("가게 상세 조회 성공", res));
    }

    @GetMapping
    public ResponseEntity<CommonResponse<Page<StoreSummaryResponse>>> getStores(
        @RequestParam(required = false) UUID categoryId,
        @RequestParam(required = false) String keyword,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size,
        @RequestParam(defaultValue = "createdAt,desc") String sort
    ) {
        CurrentUser user = getCurrentUser();

        // size 허용값 제한
        if (size != 10 && size != 30 && size != 50) {
            size = 10;
        }

        StoreSearchCondition condition = new StoreSearchCondition();
        condition.setCategoryId(categoryId);
        condition.setKeyword(keyword);
        condition.setSort(sort);

        String[] parts = sort.split(",");
        String sortKey = parts[0];
        String direction = parts.length > 1 ? parts[1] : "desc"; // 

        String property = "createdAt"; // 
        if ("rating".equalsIgnoreCase(sortKey)) {
            property = "avg_rating";
        } else if ("reviewCount".equalsIgnoreCase(sortKey)) {
            property = "review_count";
        }

        // asc / desc 방향 지정 (기본 desc)
        Sort.Direction dir =
            "asc".equalsIgnoreCase(direction) ? Sort.Direction.ASC : Sort.Direction.DESC;

        Sort sortOption = Sort.by(dir, property);

        Pageable pageable = PageRequest.of(page, size, sortOption);

        Page<StoreSummaryResponse> result = storeService.getStores(condition, pageable, user);
        return ResponseEntity.ok(CommonResponse.success("가게 목록 조회 성공", result));
    }
}