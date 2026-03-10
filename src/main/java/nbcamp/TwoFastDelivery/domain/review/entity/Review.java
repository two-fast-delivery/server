package nbcamp.TwoFastDelivery.domain.review.entity;

import jakarta.persistence.*;
import lombok.*;
import nbcamp.TwoFastDelivery.domain.review.enums.ReviewStatus;
import nbcamp.TwoFastDelivery.global.common.BaseEntity;
import org.hibernate.annotations.UuidGenerator;

import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Table(name = "p_review")
public class Review extends BaseEntity {

    @Id
    @GeneratedValue
    @UuidGenerator
    private UUID id;

    private Long userId; // -> 수정 요망 Long

    private Long storeId; // -> 수정 요망 Long

    private Long orderId; // -> 수정 요망 Long, nullable = false 3개 다 처리

    @Column(nullable = false)
    private Integer rating;

    @Column(length = 200)
    private String content;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ReviewStatus status;

    public void update(Integer rating, String content) {
        this.rating = rating;
        this.content = content;
    }

    public void delete(ReviewStatus status) {
        this.status = status;
    }
}
