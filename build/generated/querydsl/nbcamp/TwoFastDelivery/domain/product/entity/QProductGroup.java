package nbcamp.TwoFastDelivery.domain.product.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QProductGroup is a Querydsl query type for ProductGroup
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QProductGroup extends EntityPathBase<ProductGroup> {

    private static final long serialVersionUID = 317045821L;

    public static final QProductGroup productGroup = new QProductGroup("productGroup");

    public final nbcamp.TwoFastDelivery.global.common.QBaseEntity _super = new nbcamp.TwoFastDelivery.global.common.QBaseEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    //inherited
    public final ComparablePath<java.util.UUID> createdBy = _super.createdBy;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> deletedAt = _super.deletedAt;

    //inherited
    public final ComparablePath<java.util.UUID> deletedBy = _super.deletedBy;

    public final ComparablePath<java.util.UUID> id = createComparable("id", java.util.UUID.class);

    public final StringPath name = createString("name");

    public final NumberPath<Integer> sortOrder = createNumber("sortOrder", Integer.class);

    public final ComparablePath<java.util.UUID> storeId = createComparable("storeId", java.util.UUID.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    //inherited
    public final ComparablePath<java.util.UUID> updatedBy = _super.updatedBy;

    public QProductGroup(String variable) {
        super(ProductGroup.class, forVariable(variable));
    }

    public QProductGroup(Path<? extends ProductGroup> path) {
        super(path.getType(), path.getMetadata());
    }

    public QProductGroup(PathMetadata metadata) {
        super(ProductGroup.class, metadata);
    }

}

