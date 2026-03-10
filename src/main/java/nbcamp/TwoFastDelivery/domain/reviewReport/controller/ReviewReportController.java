package nbcamp.TwoFastDelivery.domain.reviewReport.controller;

import lombok.RequiredArgsConstructor;
import nbcamp.TwoFastDelivery.domain.reviewReport.dto.ReviewReportCreateRequestDto;
import nbcamp.TwoFastDelivery.domain.reviewReport.dto.ReviewReportCreateResponseDto;
import nbcamp.TwoFastDelivery.domain.reviewReport.service.ReviewReportService;
import nbcamp.TwoFastDelivery.global.common.CommonResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("reports")
public class ReviewReportController {

    private final ReviewReportService reviewReportService;

    // 신고 생성
    @PostMapping("/{reviewId}")
    public ResponseEntity<?> createReport(@PathVariable UUID reviewId, @RequestBody ReviewReportCreateRequestDto requestDto) {
        ReviewReportCreateResponseDto data = reviewReportService.createReport(reviewId, requestDto);

        return ResponseEntity.ok(CommonResponse.success("신고가 접수되었습니다.",data));
    }
}
