package nbcamp.TwoFastDelivery.domain.reviewReport.repository;

import nbcamp.TwoFastDelivery.domain.reviewReport.entity.ReviewReport;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ReviewReportRepository extends JpaRepository<ReviewReport, UUID> {
}
