package nbcamp.TwoFastDelivery.domain.review.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import nbcamp.TwoFastDelivery.domain.review.entity.Review;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@AllArgsConstructor
@Builder
public class FindReviewByStoreResponseDto {
    private UUID reviewId;
    private Long nickname; //수정 -> String
    private int rating;
    private String content;
    private LocalDateTime createdAt;

    public static FindReviewByStoreResponseDto from(Review review) {
        return FindReviewByStoreResponseDto.builder()
                .reviewId(review.getId())
                .nickname(review.getUserId()) // 수정 -> 추후 usernickname 한번 더 꺼내야 함
                .rating(review.getRating())
                .content(review.getContent())
                .createdAt(review.getCreatedAt())
                .build();
    }
}
