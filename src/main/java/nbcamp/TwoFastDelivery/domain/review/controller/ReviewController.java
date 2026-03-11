package nbcamp.TwoFastDelivery.domain.review.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import nbcamp.TwoFastDelivery.domain.review.dto.CreateReviewRequestDto;
import nbcamp.TwoFastDelivery.domain.review.dto.CreateReviewResponseDto;
import nbcamp.TwoFastDelivery.domain.review.dto.DetailReviewResponseDto;
import nbcamp.TwoFastDelivery.domain.review.dto.FindMyReviewResponsePageDto;
import nbcamp.TwoFastDelivery.domain.review.dto.FindReviewByStoreResponsePageDto;
import nbcamp.TwoFastDelivery.domain.review.dto.UpdateReviewRequestDto;
import nbcamp.TwoFastDelivery.domain.review.dto.UpdateReviewResponseDto;
import nbcamp.TwoFastDelivery.domain.review.service.ReviewService;
import nbcamp.TwoFastDelivery.global.common.CommonResponse;
import nbcamp.TwoFastDelivery.store.application.CurrentUser;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/reviews")
public class ReviewController {

    private final ReviewService reviewService;

    // 리뷰 생성
    @PostMapping
    public ResponseEntity<?> createReview(
            @AuthenticationPrincipal CurrentUser user,
            @Valid @RequestBody CreateReviewRequestDto requestDto
    ) {
        CreateReviewResponseDto data = reviewService.createReview(user.id(), requestDto);

        return ResponseEntity.ok(
                CommonResponse.success("리뷰 작성이 완료되었습니다", data)
        );
    }

    // 리뷰 수정
    @PatchMapping("/{reviewId}")
    public ResponseEntity<?> updateReview(
            @AuthenticationPrincipal CurrentUser user,
            @PathVariable UUID reviewId,
            @RequestBody UpdateReviewRequestDto updateReviewRequest
    ) {
        UpdateReviewResponseDto data =
                reviewService.updateReview(reviewId, user.id(), updateReviewRequest);

        return ResponseEntity.ok(
                CommonResponse.success("리뷰 수정이 완료되었습니다", data)
        );
    }

    // 리뷰 상세 조회
    @GetMapping("/{reviewId}")
    public ResponseEntity<?> detailReview(@PathVariable UUID reviewId) {
        DetailReviewResponseDto data = reviewService.detailReview(reviewId);

        return ResponseEntity.ok(
                CommonResponse.success("리뷰 상세 조회 성공", data)
        );
    }

    // 가게별 리뷰 조회
    @GetMapping("/stores/{storeId}")
    public ResponseEntity<?> getStoreReviews(
            @PathVariable UUID storeId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "latest") String sort
    ) {
        FindReviewByStoreResponsePageDto data =
                reviewService.storeReview(storeId, page, size, sort);

        return ResponseEntity.ok(
                CommonResponse.success("리뷰 가게 기준 조회 성공", data)
        );
    }

    // 내 리뷰 조회
    @GetMapping("/me")
    public ResponseEntity<?> getMyReviews(
            @AuthenticationPrincipal CurrentUser user,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "latest") String sort
    ) {
        FindMyReviewResponsePageDto data =
                reviewService.myReview(user.id(), page, size, sort);

        return ResponseEntity.ok(
                CommonResponse.success("내 리뷰 내역 조회 성공", data)
        );
    }

    // 리뷰 삭제
    @DeleteMapping("/{reviewId}")
    public ResponseEntity<?> deleteReview(
            @AuthenticationPrincipal CurrentUser user,
            @PathVariable UUID reviewId
    ) {
        reviewService.deleteReview(reviewId, user.id());

        return ResponseEntity.ok(
                CommonResponse.success("리뷰 삭제가 완료되었습니다")
        );
    }
}