package nbcamp.TwoFastDelivery.domain.order.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QOrderDetail is a Querydsl query type for OrderDetail
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QOrderDetail extends EntityPathBase<OrderDetail> {

    private static final long serialVersionUID = 1198328115L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QOrderDetail orderDetail = new QOrderDetail("orderDetail");

    public final QAmount amount;

    public final ComparablePath<java.util.UUID> id = createComparable("id", java.util.UUID.class);

    public final ComparablePath<java.util.UUID> menuId = createComparable("menuId", java.util.UUID.class);

    public final StringPath menuOption = createString("menuOption");

    public final QOrder order;

    public final NumberPath<Integer> quantity = createNumber("quantity", Integer.class);

    public QOrderDetail(String variable) {
        this(OrderDetail.class, forVariable(variable), INITS);
    }

    public QOrderDetail(Path<? extends OrderDetail> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QOrderDetail(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QOrderDetail(PathMetadata metadata, PathInits inits) {
        this(OrderDetail.class, metadata, inits);
    }

    public QOrderDetail(Class<? extends OrderDetail> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.amount = inits.isInitialized("amount") ? new QAmount(forProperty("amount")) : null;
        this.order = inits.isInitialized("order") ? new QOrder(forProperty("order"), inits.get("order")) : null;
    }

}

