package nbcamp.TwoFastDelivery.store.presentation;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import nbcamp.TwoFastDelivery.global.common.CommonResponse;
import nbcamp.TwoFastDelivery.global.exception.CustomException;
import nbcamp.TwoFastDelivery.global.exception.ErrorCode;
import nbcamp.TwoFastDelivery.store.application.CurrentUser;
import nbcamp.TwoFastDelivery.store.application.StoreService;
import nbcamp.TwoFastDelivery.store.application.dto.StoreSummaryResponse;
import nbcamp.TwoFastDelivery.user.domain.user.User;
import nbcamp.TwoFastDelivery.user.domain.user.UserRepository;

@RestController
@RequestMapping("/users/me")
@RequiredArgsConstructor
public class UserMeStoreController {

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

    @GetMapping("/stores")
    public ResponseEntity<CommonResponse<List<StoreSummaryResponse>>> getMyStores(){
        CurrentUser user = getCurrentUser();
        List<StoreSummaryResponse> result = storeService.getMyStores(user);

        return ResponseEntity.ok(CommonResponse.success("내 가게 목록 조회 성공", result));
    }
    
}
