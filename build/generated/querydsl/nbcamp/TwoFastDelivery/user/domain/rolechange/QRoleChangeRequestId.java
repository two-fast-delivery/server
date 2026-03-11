package nbcamp.TwoFastDelivery.user.domain.rolechange;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QRoleChangeRequestId is a Querydsl query type for RoleChangeRequestId
 */
@Generated("com.querydsl.codegen.DefaultEmbeddableSerializer")
public class QRoleChangeRequestId extends BeanPath<RoleChangeRequestId> {

    private static final long serialVersionUID = -516523314L;

    public static final QRoleChangeRequestId roleChangeRequestId = new QRoleChangeRequestId("roleChangeRequestId");

    public final ComparablePath<java.util.UUID> id = createComparable("id", java.util.UUID.class);

    public QRoleChangeRequestId(String variable) {
        super(RoleChangeRequestId.class, forVariable(variable));
    }

    public QRoleChangeRequestId(Path<? extends RoleChangeRequestId> path) {
        super(path.getType(), path.getMetadata());
    }

    public QRoleChangeRequestId(PathMetadata metadata) {
        super(RoleChangeRequestId.class, metadata);
    }

}

