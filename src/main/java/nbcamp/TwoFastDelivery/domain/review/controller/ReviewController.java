package nbcamp.TwoFastDelivery.domain.review.controller;

import lombok.RequiredArgsConstructor;
import nbcamp.TwoFastDelivery.domain.review.dto.CreateReviewRequestDto;
import nbcamp.TwoFastDelivery.domain.review.dto.ReviewResponseDto;
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
    public ResponseEntity<?> createReview(@RequestHeader("userId") Long userId, @RequestBody CreateReviewRequestDto requestDto) {
        ReviewResponseDto data = reviewService.createReview(userId, requestDto);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(CommonResponse.success("리뷰 작성이 완료되었습니다",data));
    }

}
