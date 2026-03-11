package nbcamp.TwoFastDelivery.domain.product.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QProductOptionGroup is a Querydsl query type for ProductOptionGroup
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QProductOptionGroup extends EntityPathBase<ProductOptionGroup> {

    private static final long serialVersionUID = -1848869304L;

    public static final QProductOptionGroup productOptionGroup = new QProductOptionGroup("productOptionGroup");

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

    public final NumberPath<Integer> maxSelection = createNumber("maxSelection", Integer.class);

    public final NumberPath<Integer> minSelection = createNumber("minSelection", Integer.class);

    public final StringPath name = createString("name");

    public final ComparablePath<java.util.UUID> productId = createComparable("productId", java.util.UUID.class);

    public final NumberPath<Integer> sortOrder = createNumber("sortOrder", Integer.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    //inherited
    public final ComparablePath<java.util.UUID> updatedBy = _super.updatedBy;

    public QProductOptionGroup(String variable) {
        super(ProductOptionGroup.class, forVariable(variable));
    }

    public QProductOptionGroup(Path<? extends ProductOptionGroup> path) {
        super(path.getType(), path.getMetadata());
    }

    public QProductOptionGroup(PathMetadata metadata) {
        super(ProductOptionGroup.class, metadata);
    }

}

