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
import nbcamp.TwoFastDelivery.user.domain.user.User;
import nbcamp.TwoFastDelivery.user.domain.user.UserRepository;
import nbcamp.TwoFastDelivery.global.exception.CustomException;
import nbcamp.TwoFastDelivery.global.exception.ErrorCode;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/store-requests")
public class AdminStoreRequestController {
    
    private final StoreRequestService storeRequestService;
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

    @PatchMapping("/{id}")
    public ResponseEntity<CommonResponse<Void>> decide(
        @PathVariable("id")UUID id, @RequestBody @Valid StoreRequestDecisionRequest request){
            CurrentUser admin = getCurrentUser(); //이후 수정
        storeRequestService.decideRequest(id, request, admin);

        return ResponseEntity.ok(CommonResponse.success("가게 요청이 처리되었습니다.", null));
    }
}
