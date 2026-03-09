package nbcamp.TwoFastDelivery.user.domain.rolechange;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import nbcamp.TwoFastDelivery.user.domain.UserId;
import nbcamp.TwoFastDelivery.user.domain.UserRole;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "p_user_role_change_request")
public class UserRoleChangeRequest {

    @EmbeddedId
    private RoleChangeRequestId id;

    @Embedded
    @AttributeOverride(name = "id", column = @Column(name = "user_id", nullable = false)) // user_id 컬럼으로 저장
    private UserId userId;

    @Enumerated(EnumType.STRING)
    @Column(name = "current_role", nullable = false)
    private UserRole currentRole;

    @Enumerated(EnumType.STRING)
    @Column(name = "requested_role", nullable = false)
    private UserRole requestedRole;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private UserRoleChangeStatus status;

    @Column(name = "reason", length = 500)
    private String reason;

    @Embedded
    @AttributeOverride(name = "id", column = @Column(name = "processed_by")) // processed_by 컬럼으로 저장
    private UserId processedBy;

    @Column(name = "processed_at")
    private LocalDateTime processedAt;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    public static UserRoleChangeRequest create(UserId userId, UserRole currentRole, UserRole requestedRole, String reason) { // 생성 팩토리
        UserRoleChangeRequest req = new UserRoleChangeRequest();

        req.id = RoleChangeRequestId.newId();
        req.userId = userId;
        req.currentRole = currentRole;
        req.requestedRole = requestedRole;
        req.status = UserRoleChangeStatus.PENDING;
        req.reason = reason;

        req.createdAt = LocalDateTime.now();
        req.updatedAt = LocalDateTime.now();

        return req;
    }

    public void approve(UserId adminId) { // 승인 처리
        this.status = UserRoleChangeStatus.APPROVED; // 승인 상태로 변경
        this.processedBy = adminId; // 처리자 기록
        this.processedAt = LocalDateTime.now(); // 처리 시간 기록
        this.updatedAt = LocalDateTime.now(); // 갱신 시간 업데이트
    }

    public void reject(UserId adminId) { // 거절 처리
        this.status = UserRoleChangeStatus.REJECTED; // 거절 상태로 변경
        this.processedBy = adminId; // 처리자 기록
        this.processedAt = LocalDateTime.now(); // 처리 시간 기록
        this.updatedAt = LocalDateTime.now(); // 갱신 시간 업데이트
    }
}

