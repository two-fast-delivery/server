package nbcamp.TwoFastDelivery.domain.review.controller;

import jakarta.validation.Valid;
import jakarta.websocket.server.PathParam;
import lombok.RequiredArgsConstructor;
import nbcamp.TwoFastDelivery.domain.review.dto.*;
import nbcamp.TwoFastDelivery.domain.review.service.ReviewService;
import nbcamp.TwoFastDelivery.global.common.CommonResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/reviews")
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping
    public ResponseEntity<?> createReview(@RequestHeader("userId") Long userId,@Valid @RequestBody CreateReviewRequestDto requestDto) {
        CreateReviewResponseDto data = reviewService.createReview(userId, requestDto);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(CommonResponse.success("리뷰 작성이 완료되었습니다",data));
    }

    @PatchMapping("/{reviewId}")
    public  ResponseEntity<?> updateReview(@RequestHeader("userId") Long userId, @PathVariable UUID reviewId, @RequestBody UpdateReviewRequestDto updateReviewRequest) {
        UpdateReviewResponseDto data  = reviewService.updateReview(reviewId, updateReviewRequest);

        return ResponseEntity.ok(
                CommonResponse.success("리뷰 수정이 완료되었습니다",data)
        );
    }

    @GetMapping("/{reviewId}")
    public ResponseEntity<?> detailReview(@PathVariable UUID reviewId) {
        DetailReviewResponseDto data = reviewService.detailReview(reviewId);

        return ResponseEntity.ok(
                CommonResponse.success("리뷰 상세 조회 성공",data)
        );
    }

    @DeleteMapping("/{reviewId}")
    public ResponseEntity<?> deleteReview(@PathVariable UUID reviewId) {

        return ResponseEntity.ok(
                CommonResponse.success("리뷰 삭제가 완료되었습니다.")
        );
    }
}
