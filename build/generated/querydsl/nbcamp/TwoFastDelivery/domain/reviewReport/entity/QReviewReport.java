package nbcamp.TwoFastDelivery.domain.reviewReport.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QReviewReport is a Querydsl query type for ReviewReport
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QReviewReport extends EntityPathBase<ReviewReport> {

    private static final long serialVersionUID = 219359412L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QReviewReport reviewReport = new QReviewReport("reviewReport");

    public final nbcamp.TwoFastDelivery.global.common.QBaseEntity _super = new nbcamp.TwoFastDelivery.global.common.QBaseEntity(this);

    public final StringPath content = createString("content");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    //inherited
    public final ComparablePath<java.util.UUID> createdBy = _super.createdBy;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> deletedAt = _super.deletedAt;

    //inherited
    public final ComparablePath<java.util.UUID> deletedBy = _super.deletedBy;

    public final ComparablePath<java.util.UUID> id = createComparable("id", java.util.UUID.class);

    public final nbcamp.TwoFastDelivery.domain.review.entity.QReview review;

    public final EnumPath<nbcamp.TwoFastDelivery.domain.reviewReport.enums.ReviewReportStatus> status = createEnum("status", nbcamp.TwoFastDelivery.domain.reviewReport.enums.ReviewReportStatus.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    //inherited
    public final ComparablePath<java.util.UUID> updatedBy = _super.updatedBy;

    public QReviewReport(String variable) {
        this(ReviewReport.class, forVariable(variable), INITS);
    }

    public QReviewReport(Path<? extends ReviewReport> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QReviewReport(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QReviewReport(PathMetadata metadata, PathInits inits) {
        this(ReviewReport.class, metadata, inits);
    }

    public QReviewReport(Class<? extends ReviewReport> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.review = inits.isInitialized("review") ? new nbcamp.TwoFastDelivery.domain.review.entity.QReview(forProperty("review")) : null;
    }

}

