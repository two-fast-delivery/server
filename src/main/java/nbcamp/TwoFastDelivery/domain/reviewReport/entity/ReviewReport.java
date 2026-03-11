package nbcamp.TwoFastDelivery.domain.reviewReport.entity;

import jakarta.persistence.*;
import lombok.*;
import nbcamp.TwoFastDelivery.domain.review.entity.Review;
import nbcamp.TwoFastDelivery.domain.reviewReport.enums.ReviewReportStatus;
import nbcamp.TwoFastDelivery.global.common.BaseEntity;
import org.hibernate.annotations.UuidGenerator;

import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Table(name = "p_review_report")
public class ReviewReport extends BaseEntity {

    @Id
    @GeneratedValue
    @UuidGenerator
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "review_id")
    private Review review;

    @Column(nullable = false)
    private String content;

    @Builder.Default
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ReviewReportStatus status = ReviewReportStatus.PENDING;

    public void setStatusReport(ReviewReportStatus status) {
        this.status = status;
    }
}
