package nbcamp.TwoFastDelivery.domain.review.service;

import lombok.RequiredArgsConstructor;
import nbcamp.TwoFastDelivery.domain.review.dto.*;
import nbcamp.TwoFastDelivery.domain.review.entity.Review;
import nbcamp.TwoFastDelivery.domain.review.enums.ReviewStatus;
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
    // userId 나중에 토큰에서 세팅  userId Long -> UUID로 수정
    public CreateReviewResponseDto createReview(Long userId, CreateReviewRequestDto requestDto) {
        if (reviewRepository.existsByOrderId(requestDto.getOrderId())) {
            throw new CustomException(ErrorCode.REVIEW_ALREADY_EXISTS);
        }

        Review review = Review.builder()
                .userId(userId)
                .storeId(null) //-> 수정 요망 orederId에서 storeId 세팅
                .orderId(requestDto.getOrderId())
                .rating(requestDto.getRating())
                .content(requestDto.getContent())
                .status(ReviewStatus.ACTIVE)
                .build();

        Review savedReview = reviewRepository.save(review);

        CreateReviewResponseDto responseDto = new CreateReviewResponseDto(
                "가게 이름",//-> 수정요망 store 세팅
                savedReview.getId(),
                savedReview.getRating(),
                savedReview.getContent(),
                savedReview.getCreatedAt()
        );

        return responseDto;
    }

    //리뷰 수정
    public UpdateReviewResponseDto updateReview(UUID reviewId, UpdateReviewRequestDto updateReviewRequest) {
         Review review = reviewRepository.findById(reviewId)
                 .orElseThrow(() -> new CustomException(ErrorCode.REVIEW_NOT_EXISTS));

         //리뷰가 활성화 상태 아닐 때
         if (review.getStatus()!= ReviewStatus.ACTIVE) {
             throw new CustomException(ErrorCode.REVIEW_NOT_ACTIVE);
         }

         review.update(updateReviewRequest.getRating(), updateReviewRequest.getContent());

          return new UpdateReviewResponseDto(
                  reviewId,
                  review.getRating(),
                  review.getContent()
          );
    }

    //리뷰 상세 조회
    public DetailReviewResponseDto detailReview(UUID reviewId) {
        //reviewId에 해당하는 리뷰가 존재하지 않을 때
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(()->new CustomException(ErrorCode.REVIEW_NOT_EXISTS));

        return new DetailReviewResponseDto(reviewId,
                review.getStatus(),
                review.getStoreId(),
                review.getUserId(),
                review.getRating(),
                review.getContent(),
                review.getCreatedAt(),
                review.getUpdatedAt()
        );
    }
    //리뷰 삭제
    public void deleteReview(UUID reviewId, Long userId) {
        //reviewId에 해당하는 리뷰가 존재하지 않을 떄
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(()->new CustomException(ErrorCode.REVIEW_NOT_EXISTS));
        //reviewId-userId와 로그인 되어있는 userId가 일치하지 않을 때
        if (review.getUserId()!=userId) {
            throw new CustomException(ErrorCode.REVIEW_NOT_EQUAL_USER);
        }

        review.delete(ReviewStatus.DELETE);
    }
}
