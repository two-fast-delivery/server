package nbcamp.TwoFastDelivery.domain.review.service;

import lombok.RequiredArgsConstructor;
import nbcamp.TwoFastDelivery.domain.review.dto.CreateReviewRequestDto;
import nbcamp.TwoFastDelivery.domain.review.dto.ReviewResponseDto;
import nbcamp.TwoFastDelivery.domain.review.entity.Review;
import nbcamp.TwoFastDelivery.domain.review.repository.ReviewRepository;
import nbcamp.TwoFastDelivery.global.exception.CustomException;
import nbcamp.TwoFastDelivery.global.exception.ErrorCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class ReviewService {

    private final ReviewRepository reviewRepository;


    // 리뷰 생성
    // userId 나중에 토큰에서 세팅
    public ReviewResponseDto createReview(Long userId, CreateReviewRequestDto requestDto) {
        if (reviewRepository.existsByOrderId(requestDto.getOrderId())) {
            throw new CustomException(ErrorCode.REVIEW_ALREADY_EXISTS); //예외사항 규격 맞춰서 처리
        }

        Review review = Review.builder()
                .userId(userId)
                .storeId(null) // orederId에서 storeId 세팅
                .orderId(requestDto.getOrderId())
                .rating(requestDto.getRating())
                .content(requestDto.getContent())
                .build();

        Review savedReview = reviewRepository.save(review);

        ReviewResponseDto responseDto = new ReviewResponseDto(
                "가게 이름",//store 세팅
                savedReview.getId(),
                savedReview.getRating(),
                savedReview.getContent(),
                savedReview.getCreatedAt()
        );

        return responseDto;
    }
}
