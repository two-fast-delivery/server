package nbcamp.TwoFastDelivery.domain.review.service;

import lombok.RequiredArgsConstructor;
import nbcamp.TwoFastDelivery.domain.order.entity.Order;
import nbcamp.TwoFastDelivery.domain.order.repository.OrderRepository;
import nbcamp.TwoFastDelivery.domain.review.dto.*;
import nbcamp.TwoFastDelivery.domain.review.entity.Review;
import nbcamp.TwoFastDelivery.domain.review.enums.ReviewStatus;
import nbcamp.TwoFastDelivery.domain.review.event.ReviewCreatedEvent;
import nbcamp.TwoFastDelivery.domain.review.event.ReviewDeletedEvent;
import nbcamp.TwoFastDelivery.domain.review.event.ReviewUpdatedEvent;
import nbcamp.TwoFastDelivery.domain.review.repository.ReviewRepository;
import nbcamp.TwoFastDelivery.global.exception.CustomException;
import nbcamp.TwoFastDelivery.global.exception.ErrorCode;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final OrderRepository orderRepository;
    private final ApplicationEventPublisher eventPublisher;

    // 리뷰 생성
    //TODO:인증 기능 작동 시 토큰에서 userId 발급, error 추가
    public CreateReviewResponseDto createReview(UUID userId, CreateReviewRequestDto requestDto) {
        Order order = orderRepository.findById(requestDto.getOrderId())
                .orElseThrow(()->new CustomException(ErrorCode.NOT_FOUND));
        Review review = Review.builder()
                .userId(userId)
                .storeId(order.getStoreId())
                .orderId(requestDto.getOrderId())
                .rating(requestDto.getRating())
                .content(requestDto.getContent())
                .status(ReviewStatus.ACTIVE)
                .build();

        Review savedReview = reviewRepository.save(review);

        //이벤트 발행
        eventPublisher.publishEvent(
                new ReviewCreatedEvent(savedReview.getId(), review.getStoreId())
        );

        CreateReviewResponseDto responseDto = new CreateReviewResponseDto(
                savedReview.getId(),
                savedReview.getRating(),
                savedReview.getContent(),
                savedReview.getCreatedAt()
        );
        return responseDto;
    }

    //리뷰 수정
    public UpdateReviewResponseDto updateReview(UUID reviewId,UUID userId, UpdateReviewRequestDto updateReviewRequest) {
         Review review = reviewRepository.findById(reviewId)
                 .orElseThrow(() -> new CustomException(ErrorCode.REVIEW_NOT_EXISTS));

         //작성자와 로그인 유저가 일치하지 않을 때
        if (!review.getUserId().equals(userId)) {
            throw new CustomException(ErrorCode.REVIEW_NOT_EQUAL_USER);
        }

         //리뷰가 비활성화 상태일 때
         if (review.getStatus()!= ReviewStatus.ACTIVE) {
             throw new CustomException(ErrorCode.REVIEW_NOT_ACTIVE);
         }

         review.update(updateReviewRequest.getRating(), updateReviewRequest.getContent());

         //이벤트 발행
        eventPublisher.publishEvent(
                new ReviewUpdatedEvent(reviewId, review.getStoreId())
        );

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

    //리뷰 가게 기준 조회
    public FindReviewByStoreResponsePageDto storeReview(UUID storeId, int page, int size, String sort) {
        //Todo: storeId 검증 작업? 필요할까?
        Pageable pageable = createPageable(page, size, sort);

        Page<Review> reviewPage = reviewRepository.findReviewByStoreIdAndStatus(storeId, ReviewStatus.ACTIVE, pageable);

        List<FindReviewByStoreResponseDto> reviews = reviewPage.getContent().stream()
                .map(FindReviewByStoreResponseDto::from)
                .toList();

        return FindReviewByStoreResponsePageDto.of(reviewPage, reviews);
    }

    //리뷰 본인 내역 조회
    public FindMyReviewResponsePageDto myReview(UUID userId,int page, int size, String sort) {
        Pageable pageable = createPageable(page, size, sort);
        Page<Review> reviewPage = reviewRepository.findReviewByUserId(userId, pageable);

        List<FindReviewByStoreResponseDto> reviews = reviewPage.getContent().stream()
                .map(FindReviewByStoreResponseDto::from)
                .toList();

        return FindMyReviewResponsePageDto.of(reviewPage, reviews);
    }


    //리뷰 삭제
    public void deleteReview(UUID reviewId, UUID userId) {
        //reviewId에 해당하는 리뷰가 존재하지 않을 떄
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(()->new CustomException(ErrorCode.REVIEW_NOT_EXISTS));

        //reviewId-userId와 로그인 되어있는 userId가 일치하지 않을 때
        if (!review.getUserId().equals(userId)) {
            throw new CustomException(ErrorCode.REVIEW_NOT_EQUAL_USER);
        }

        review.setReviewStatus(ReviewStatus.DELETE);

        //이벤트 발행
        eventPublisher.publishEvent(
                new ReviewDeletedEvent(reviewId, review.getStoreId())
        );
    }

    //Pageable 평점순,오래된순, 최신순
    private Pageable createPageable(int page, int size, String sort) {
        int validatedSize = validateSize(size);

        return switch (sort) {
            case "ratingDesc" -> PageRequest.of(page, validatedSize, Sort.by(Sort.Direction.DESC, "rating"));
            case "oldest" -> PageRequest.of(page, validatedSize, Sort.by(Sort.Direction.ASC, "createdAt"));
            case "latest" -> PageRequest.of(page, validatedSize, Sort.by(Sort.Direction.DESC, "createdAt"));
            default -> PageRequest.of(page, validatedSize, Sort.by(Sort.Direction.DESC, "createdAt"));
        };
    }

    private int validateSize(int size) {
        if (size == 10 || size == 30 || size == 50) {
            return size;
        }
        return 10;
    }


}
