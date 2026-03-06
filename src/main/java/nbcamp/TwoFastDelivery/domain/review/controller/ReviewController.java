package nbcamp.TwoFastDelivery.domain.review.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import nbcamp.TwoFastDelivery.domain.review.dto.CreateReviewRequestDto;
import nbcamp.TwoFastDelivery.domain.review.dto.CreateReviewResponseDto;
import nbcamp.TwoFastDelivery.domain.review.dto.UpdateReviewRequestDto;
import nbcamp.TwoFastDelivery.domain.review.dto.UpdateReviewResponseDto;
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
}
