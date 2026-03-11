package nbcamp.TwoFastDelivery.domain.review.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import nbcamp.TwoFastDelivery.domain.review.dto.*;
import nbcamp.TwoFastDelivery.domain.review.service.ReviewService;
import nbcamp.TwoFastDelivery.global.common.CommonResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/reviews")
public class ReviewController {

    private final ReviewService reviewService;

    //리뷰 생성
    @PostMapping
    public ResponseEntity<?> createReview(@RequestHeader("userId") UUID userId,@Valid @RequestBody CreateReviewRequestDto requestDto) {
        CreateReviewResponseDto data = reviewService.createReview(userId, requestDto); //todo: PathVariable -> (requestHeader에서 token으로 UUID형태 userId받기)

        return ResponseEntity.ok(
                CommonResponse.success("리뷰 작성이 완료되었습니다",data));
    }

    //리뷰 수정
    //todo:유저 아이디 토큰에서 꺼내기
    @PatchMapping("/{reviewId}")
    public  ResponseEntity<?> updateReview(@RequestHeader("userId") UUID userId, @PathVariable UUID reviewId, @RequestBody UpdateReviewRequestDto updateReviewRequest) {
        UpdateReviewResponseDto data  = reviewService.updateReview(reviewId,userId, updateReviewRequest);

        return ResponseEntity.ok(
                CommonResponse.success("리뷰 수정이 완료되었습니다",data)
        );
    }

    //리뷰 상세 조회
    @GetMapping("/{reviewId}")
    public ResponseEntity<?> detailReview(@PathVariable UUID reviewId) {
        DetailReviewResponseDto data = reviewService.detailReview(reviewId);

        return ResponseEntity.ok(
                CommonResponse.success("리뷰 상세 조회 성공",data)
        );
    }

    //리뷰 가게 기준 조회
    @GetMapping("/stores/{storeId}")
    public ResponseEntity<?> getStoreReviews(
            @PathVariable UUID storeId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "latest") String sort
    ) {
        FindReviewByStoreResponsePageDto data = reviewService.storeReview(storeId, page, size, sort);
        return ResponseEntity.ok(
                CommonResponse.success("리뷰 가게 기준 조회 성공", data)
        );
    }

    //리뷰 유저 기준 조회
    @GetMapping("/users/{userId}")
    public ResponseEntity<?> getMyReviews (
            @PathVariable UUID userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "latest") String sort
    ){
      FindMyReviewResponsePageDto data = reviewService.myReview(userId, page, size, sort);
      return ResponseEntity.ok(
              CommonResponse.success("내 리뷰 내역 조회 성공", data)
      );
    }

    //리뷰 삭제
    @DeleteMapping("/{reviewId}")
    public ResponseEntity<?> deleteReview(@PathVariable UUID reviewId) {

        return ResponseEntity.ok(
                CommonResponse.success("리뷰 삭제가 완료되었습니다.")
        );
    }
}
