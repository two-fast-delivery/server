package nbcamp.TwoFastDelivery.domain.reviewReport.service;

import lombok.RequiredArgsConstructor;
import nbcamp.TwoFastDelivery.domain.review.entity.Review;
import nbcamp.TwoFastDelivery.domain.review.enums.ReviewStatus;
import nbcamp.TwoFastDelivery.domain.review.repository.ReviewRepository;
import nbcamp.TwoFastDelivery.domain.reviewReport.dto.*;
import nbcamp.TwoFastDelivery.domain.reviewReport.entity.ReviewReport;
import nbcamp.TwoFastDelivery.domain.reviewReport.enums.ReviewReportStatus;
import nbcamp.TwoFastDelivery.domain.reviewReport.repository.ReviewReportRepository;
import nbcamp.TwoFastDelivery.global.exception.CustomException;
import nbcamp.TwoFastDelivery.global.exception.ErrorCode;
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
public class ReviewReportService {

    private final ReviewReportRepository reviewReportRepository;
    private final ReviewRepository reviewRepository;

    //리뷰 신고 생성 (일반 사용자)
    public ReviewReportCreateResponseDto createReport (UUID reviewId, ReviewReportCreateRequestDto requestDto) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(()->new CustomException(ErrorCode.REVIEW_NOT_EXISTS));

        ReviewReport report = ReviewReport.builder()
               .review(review)
               .content(requestDto.getReportContent())
               .build();

       ReviewReport savedReport = reviewReportRepository.save(report);

       ReviewReportCreateResponseDto responseDto = new ReviewReportCreateResponseDto(
               savedReport.getId(),
               savedReport.getReview().getId()
       );

        return responseDto;
    }

    //리뷰 신고 상세 조회
    public ReviewReportDetailResponseDto detailReport (UUID reportId) {

        ReviewReport report = reviewReportRepository.findById(reportId)
                .orElseThrow(()->new CustomException(ErrorCode.REPORT_NOT_EXISTS));

        return new ReviewReportDetailResponseDto(
                reportId,
                report.getReview().getUserId(),
                report.getStatus(),
                report.getReview().getId(),
                report.getReview().getRating(),
                report.getReview().getContent(),
                report.getContent()
        );
    }

    //리뷰 신고 전체 조회
    public ReviewReportAllPageReponseDto allReport (int page, int size, String sort) {
        Pageable pageable = createPageable(page, size, sort);

        Page<ReviewReport> reportPage = reviewReportRepository.findAll(pageable);

        List<ReviewReportAllResponseDto> reports = reportPage.getContent().stream()
                .map(ReviewReportAllResponseDto::from)
                .toList();

        return ReviewReportAllPageReponseDto.of(reportPage, reports);
    }

    //신고 승인
    public void approveReport(UUID reportId) {
        ReviewReport report = reviewReportRepository.findById(reportId)
                .orElseThrow(()->new CustomException(ErrorCode.REPORT_NOT_EXISTS));

        Review review = reviewRepository.findById(report.getReview().getId())
                .orElseThrow(()->new CustomException(ErrorCode.REVIEW_NOT_EXISTS));

        report.setStatusReport(ReviewReportStatus.APPROVED);
        review.setReviewStatus(ReviewStatus.HIDDEN);
    }

    //신고 거절
    public void rejecteReport(UUID reportId) {
        ReviewReport report = reviewReportRepository.findById(reportId)
                .orElseThrow(()->new CustomException(ErrorCode.REPORT_NOT_EXISTS));

        Review review = reviewRepository.findById(report.getReview().getId())
                .orElseThrow(()->new CustomException(ErrorCode.REVIEW_NOT_EXISTS));

        report.setStatusReport(ReviewReportStatus.REJECTED);
    }

    private Pageable createPageable(int page, int size, String sort) {
        int validatedSize = validateSize(size);

        return switch (sort) {
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
