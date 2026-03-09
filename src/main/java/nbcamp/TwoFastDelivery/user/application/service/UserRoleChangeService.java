package nbcamp.TwoFastDelivery.user.application.service;

import lombok.RequiredArgsConstructor;
import nbcamp.TwoFastDelivery.user.application.dto.request.RoleChangeRequestCreateRequest;
import nbcamp.TwoFastDelivery.user.application.dto.response.RoleChangeRequestResponse;
import nbcamp.TwoFastDelivery.user.domain.user.User;
import nbcamp.TwoFastDelivery.user.domain.user.UserId;
import nbcamp.TwoFastDelivery.user.domain.rolechange.RoleChangeRequestId;
import nbcamp.TwoFastDelivery.user.domain.rolechange.UserRoleChangeRequest;
import nbcamp.TwoFastDelivery.user.domain.rolechange.UserRoleChangeStatus;
import nbcamp.TwoFastDelivery.user.infrastructure.UserJpaRepository;
import nbcamp.TwoFastDelivery.user.infrastructure.UserRoleChangeRequestRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class UserRoleChangeService {

    private final UserJpaRepository userJpaRepository;
    private final UserRoleChangeRequestRepository roleChangeRequestRepository;

    public RoleChangeRequestResponse createRoleChangeRequest(UUID userId, RoleChangeRequestCreateRequest request) {

        UserId domainUserId = new UserId(userId);

        User user = userJpaRepository.findById(domainUserId)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        if (roleChangeRequestRepository.existsByUserIdAndStatus(domainUserId, UserRoleChangeStatus.PENDING)) {
            throw new RuntimeException("이미 처리중인 권한 변경 요청이 있습니다.");
        }

        UserRoleChangeRequest roleChangeRequest =
                UserRoleChangeRequest.create(
                        domainUserId,
                        user.getRole(),
                        request.getRequestedRole(),
                        request.getReason()
                );

        roleChangeRequestRepository.save(roleChangeRequest);

        return RoleChangeRequestResponse.builder()
                .requestId(roleChangeRequest.getId().getId().toString())
                .userId(userId.toString())
                .currentRole(user.getRole().name())
                .requestedRole(request.getRequestedRole().name())
                .status(roleChangeRequest.getStatus().name())
                .build();
    }

    public void approveRoleChangeRequest(UUID requestId, UUID adminId) {

        UserRoleChangeRequest request =
                roleChangeRequestRepository.findById(RoleChangeRequestId.of(requestId))
                        .orElseThrow(() -> new RuntimeException("요청을 찾을 수 없습니다."));

        if (request.getStatus() != UserRoleChangeStatus.PENDING) {
            throw new RuntimeException("이미 처리된 요청입니다.");
        }

        User user = userJpaRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        user.changeRole(request.getRequestedRole());

        request.approve(new UserId(adminId));
    }

    public void rejectRoleChangeRequest(UUID requestId, UUID adminId) {

        UserRoleChangeRequest request =
                roleChangeRequestRepository.findById(RoleChangeRequestId.of(requestId))
                        .orElseThrow(() -> new RuntimeException("요청을 찾을 수 없습니다."));

        if (request.getStatus() != UserRoleChangeStatus.PENDING) {
            throw new RuntimeException("이미 처리된 요청입니다.");
        }

        request.reject(new UserId(adminId));
    }
}