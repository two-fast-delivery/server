package nbcamp.TwoFastDelivery.domain.payment.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QPayment is a Querydsl query type for Payment
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QPayment extends EntityPathBase<Payment> {

    private static final long serialVersionUID = 1186850434L;

    public static final QPayment payment = new QPayment("payment");

    public final nbcamp.TwoFastDelivery.global.common.QBaseEntity _super = new nbcamp.TwoFastDelivery.global.common.QBaseEntity(this);

    public final StringPath cancelReason = createString("cancelReason");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    //inherited
    public final ComparablePath<java.util.UUID> createdBy = _super.createdBy;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> deletedAt = _super.deletedAt;

    //inherited
    public final ComparablePath<java.util.UUID> deletedBy = _super.deletedBy;

    public final StringPath method = createString("method");

    public final ComparablePath<java.util.UUID> orderId = createComparable("orderId", java.util.UUID.class);

    public final ComparablePath<java.util.UUID> paymentId = createComparable("paymentId", java.util.UUID.class);

    public final StringPath paymentKey = createString("paymentKey");

    public final EnumPath<PaymentStatus> paymentStatus = createEnum("paymentStatus", PaymentStatus.class);

    public final NumberPath<Integer> totalAmount = createNumber("totalAmount", Integer.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    //inherited
    public final ComparablePath<java.util.UUID> updatedBy = _super.updatedBy;

    public final ComparablePath<java.util.UUID> userId = createComparable("userId", java.util.UUID.class);

    public QPayment(String variable) {
        super(Payment.class, forVariable(variable));
    }

    public QPayment(Path<? extends Payment> path) {
        super(path.getType(), path.getMetadata());
    }

    public QPayment(PathMetadata metadata) {
        super(Payment.class, metadata);
    }

}

