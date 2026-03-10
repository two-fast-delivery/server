package nbcamp.TwoFastDelivery.domain.reviewReport.service;

import lombok.RequiredArgsConstructor;
import nbcamp.TwoFastDelivery.domain.reviewReport.dto.ReviewReportCreateRequestDto;
import nbcamp.TwoFastDelivery.domain.reviewReport.dto.ReviewReportCreateResponseDto;
import nbcamp.TwoFastDelivery.domain.reviewReport.entity.ReviewReport;
import nbcamp.TwoFastDelivery.domain.reviewReport.repository.ReviewReportRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class ReviewReportService {

    private final ReviewReportRepository reviewReportRepository;

    //리뷰 신고 생성 (일반 사용자)
    public ReviewReportCreateResponseDto createReport (UUID reviewId, ReviewReportCreateRequestDto requestDto) {
       ReviewReport report = ReviewReport.builder()
               .reviewId(reviewId)
               .Content(requestDto.getReportContent())
               .build();

       ReviewReport savedReport = reviewReportRepository.save(report);

       ReviewReportCreateResponseDto responseDto = new ReviewReportCreateResponseDto(
               savedReport.getReviewId(),
               savedReport.getReviewId()
       );

        return responseDto;
    }
}
