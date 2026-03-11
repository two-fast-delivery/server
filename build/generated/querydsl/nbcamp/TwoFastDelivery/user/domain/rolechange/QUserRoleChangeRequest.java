package nbcamp.TwoFastDelivery.user.domain.rolechange;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QUserRoleChangeRequest is a Querydsl query type for UserRoleChangeRequest
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QUserRoleChangeRequest extends EntityPathBase<UserRoleChangeRequest> {

    private static final long serialVersionUID = -319063864L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QUserRoleChangeRequest userRoleChangeRequest = new QUserRoleChangeRequest("userRoleChangeRequest");

    public final DateTimePath<java.time.LocalDateTime> createdAt = createDateTime("createdAt", java.time.LocalDateTime.class);

    public final EnumPath<nbcamp.TwoFastDelivery.user.domain.user.UserRole> currentRole = createEnum("currentRole", nbcamp.TwoFastDelivery.user.domain.user.UserRole.class);

    public final QRoleChangeRequestId id;

    public final DateTimePath<java.time.LocalDateTime> processedAt = createDateTime("processedAt", java.time.LocalDateTime.class);

    public final nbcamp.TwoFastDelivery.user.domain.user.QUserId processedBy;

    public final StringPath reason = createString("reason");

    public final EnumPath<nbcamp.TwoFastDelivery.user.domain.user.UserRole> requestedRole = createEnum("requestedRole", nbcamp.TwoFastDelivery.user.domain.user.UserRole.class);

    public final EnumPath<UserRoleChangeStatus> status = createEnum("status", UserRoleChangeStatus.class);

    public final DateTimePath<java.time.LocalDateTime> updatedAt = createDateTime("updatedAt", java.time.LocalDateTime.class);

    public final nbcamp.TwoFastDelivery.user.domain.user.QUserId userId;

    public QUserRoleChangeRequest(String variable) {
        this(UserRoleChangeRequest.class, forVariable(variable), INITS);
    }

    public QUserRoleChangeRequest(Path<? extends UserRoleChangeRequest> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QUserRoleChangeRequest(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QUserRoleChangeRequest(PathMetadata metadata, PathInits inits) {
        this(UserRoleChangeRequest.class, metadata, inits);
    }

    public QUserRoleChangeRequest(Class<? extends UserRoleChangeRequest> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.id = inits.isInitialized("id") ? new QRoleChangeRequestId(forProperty("id")) : null;
        this.processedBy = inits.isInitialized("processedBy") ? new nbcamp.TwoFastDelivery.user.domain.user.QUserId(forProperty("processedBy")) : null;
        this.userId = inits.isInitialized("userId") ? new nbcamp.TwoFastDelivery.user.domain.user.QUserId(forProperty("userId")) : null;
    }

}

