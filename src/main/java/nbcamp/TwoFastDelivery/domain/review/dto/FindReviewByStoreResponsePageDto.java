package nbcamp.TwoFastDelivery.domain.review.dto;

import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
@Builder
public class FindReviewByStoreResponsePageDto {
    private int page;
    private int totalPages;
    private int size;
    private long totalElements;
    private List<FindReviewByStoreResponseDto> reviews;

    public static FindReviewByStoreResponsePageDto of(
            Page<?> reviewPage,
            List<FindReviewByStoreResponseDto> reviews
    ) {
        return FindReviewByStoreResponsePageDto.builder()
                .page(reviewPage.getNumber())
                .totalPages(reviewPage.getTotalPages())
                .size(reviewPage.getSize())
                .totalElements(reviewPage.getTotalElements())
                .reviews(reviews)
                .build();
    }


}
