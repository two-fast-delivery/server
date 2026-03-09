package nbcamp.TwoFastDelivery.user.infrastructure;

import nbcamp.TwoFastDelivery.user.domain.user.UserId;
import nbcamp.TwoFastDelivery.user.domain.rolechange.RoleChangeRequestId;
import nbcamp.TwoFastDelivery.user.domain.rolechange.UserRoleChangeRequest;
import nbcamp.TwoFastDelivery.user.domain.rolechange.UserRoleChangeStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRoleChangeRequestRepository extends JpaRepository<UserRoleChangeRequest, RoleChangeRequestId> {

    boolean existsByUserIdAndStatus(UserId userId, UserRoleChangeStatus status);
}