package nbcamp.TwoFastDelivery.domain.reviewReport.controller;

import lombok.RequiredArgsConstructor;
import nbcamp.TwoFastDelivery.domain.reviewReport.dto.ReviewReportAllPageReponseDto;
import nbcamp.TwoFastDelivery.domain.reviewReport.dto.ReviewReportCreateRequestDto;
import nbcamp.TwoFastDelivery.domain.reviewReport.dto.ReviewReportCreateResponseDto;
import nbcamp.TwoFastDelivery.domain.reviewReport.dto.ReviewReportDetailResponseDto;
import nbcamp.TwoFastDelivery.domain.reviewReport.service.ReviewReportService;
import nbcamp.TwoFastDelivery.global.common.CommonResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/reports")
public class ReviewReportController {

    private final ReviewReportService reviewReportService;

    // 신고 생성
    @PostMapping("/{reviewId}")
    public ResponseEntity<?> createReport(@PathVariable UUID reviewId, @RequestBody ReviewReportCreateRequestDto requestDto) {
        ReviewReportCreateResponseDto data = reviewReportService.createReport(reviewId, requestDto);

        return ResponseEntity.ok(CommonResponse.success("신고가 접수되었습니다.",data));
    }

    //신고 상세 조회
    @GetMapping("/{reportId}")
    public ResponseEntity<?> detailReport(@PathVariable UUID reportId) {
        ReviewReportDetailResponseDto data = reviewReportService.detailReport(reportId);

        return ResponseEntity.ok(CommonResponse.success("신고 상세 조회가 완료되었습니다.", data));
    }

    //신고 전체 조회
    @GetMapping
    public ResponseEntity<?> allReports(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "latest") String sort
    ) {
        ReviewReportAllPageReponseDto data = reviewReportService.allReport(page, size, sort);
        return ResponseEntity.ok(
                CommonResponse.success("신고 전체 조회에 성공", data)
        );
    }

    //신고 승인
   @PatchMapping("/{reportId}/approve")
    public ResponseEntity<?> approveReport(@PathVariable UUID reportId) {
        reviewReportService.approveReport(reportId);

        return ResponseEntity.ok(
                CommonResponse.success("신고가 승인되었습니다.")
        );
   }

   //신고 거절
   @PatchMapping("/{reportId}/reject")
   public ResponseEntity<?> rejectReport(@PathVariable UUID reportId) {
       reviewReportService.rejecteReport(reportId);

       return ResponseEntity.ok(
               CommonResponse.success("신고가 거절되었습니다.")
       );
   }

}
