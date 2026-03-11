package nbcamp.TwoFastDelivery.domain.product.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QProductOption is a Querydsl query type for ProductOption
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QProductOption extends EntityPathBase<ProductOption> {

    private static final long serialVersionUID = 1465809527L;

    public static final QProductOption productOption = new QProductOption("productOption");

    public final nbcamp.TwoFastDelivery.global.common.QBaseEntity _super = new nbcamp.TwoFastDelivery.global.common.QBaseEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    //inherited
    public final ComparablePath<java.util.UUID> createdBy = _super.createdBy;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> deletedAt = _super.deletedAt;

    //inherited
    public final ComparablePath<java.util.UUID> deletedBy = _super.deletedBy;

    public final NumberPath<Integer> extraPrice = createNumber("extraPrice", Integer.class);

    public final ComparablePath<java.util.UUID> groupId = createComparable("groupId", java.util.UUID.class);

    public final ComparablePath<java.util.UUID> id = createComparable("id", java.util.UUID.class);

    public final StringPath name = createString("name");

    public final NumberPath<Integer> sortOrder = createNumber("sortOrder", Integer.class);

    public final EnumPath<ProductOption.ProductOptionStatus> status = createEnum("status", ProductOption.ProductOptionStatus.class);

    public final NumberPath<Integer> stock = createNumber("stock", Integer.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    //inherited
    public final ComparablePath<java.util.UUID> updatedBy = _super.updatedBy;

    public QProductOption(String variable) {
        super(ProductOption.class, forVariable(variable));
    }

    public QProductOption(Path<? extends ProductOption> path) {
        super(path.getType(), path.getMetadata());
    }

    public QProductOption(PathMetadata metadata) {
        super(ProductOption.class, metadata);
    }

}

