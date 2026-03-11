package nbcamp.TwoFastDelivery.store.presentation;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
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
import nbcamp.TwoFastDelivery.global.exception.CustomException;
import nbcamp.TwoFastDelivery.global.exception.ErrorCode;
import nbcamp.TwoFastDelivery.store.application.CurrentUser;
import nbcamp.TwoFastDelivery.store.application.StoreService;
import nbcamp.TwoFastDelivery.store.application.dto.StoreUpdateRequest;
import nbcamp.TwoFastDelivery.user.domain.user.User;
import nbcamp.TwoFastDelivery.user.domain.user.UserRepository;

@RestController
@RequestMapping("/owner/stores")
@RequiredArgsConstructor
public class OwnerStoreController {
    
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


    // User-Id 변경 필요
    @PatchMapping("/{id}")
    public ResponseEntity<CommonResponse<Void>> update(
        @PathVariable UUID id, @RequestBody @Valid StoreUpdateRequest request){

        CurrentUser user = getCurrentUser();
        storeService.updateStore(id, request, user);

        return ResponseEntity.ok(CommonResponse.success("가게 정보가 수정되었습니다", null));
    }
}
