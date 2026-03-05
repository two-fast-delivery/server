package nbcamp.TwoFastDelivery.domain.review.entity;

import jakarta.persistence.*;
import lombok.*;
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

    private Long userId;

    private Long storeId;

    private Long orderId;

    private Integer rating;

    private String content;
}
