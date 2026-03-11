package nbcamp.TwoFastDelivery.store.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QStoreRequest is a Querydsl query type for StoreRequest
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QStoreRequest extends EntityPathBase<StoreRequest> {

    private static final long serialVersionUID = -2044818496L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QStoreRequest storeRequest = new QStoreRequest("storeRequest");

    public final ComparablePath<java.util.UUID> id = createComparable("id", java.util.UUID.class);

    public final EnumPath<StoreRequestType> requestType = createEnum("requestType", StoreRequestType.class);

    public final EnumPath<StoreRequestStatus> status = createEnum("status", StoreRequestStatus.class);

    public final QStore store;

    public final StringPath store_desc = createString("store_desc");

    public QStoreRequest(String variable) {
        this(StoreRequest.class, forVariable(variable), INITS);
    }

    public QStoreRequest(Path<? extends StoreRequest> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QStoreRequest(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QStoreRequest(PathMetadata metadata, PathInits inits) {
        this(StoreRequest.class, metadata, inits);
    }

    public QStoreRequest(Class<? extends StoreRequest> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.store = inits.isInitialized("store") ? new QStore(forProperty("store")) : null;
    }

}

