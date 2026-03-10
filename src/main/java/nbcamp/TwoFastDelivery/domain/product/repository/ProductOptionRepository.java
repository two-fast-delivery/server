package nbcamp.TwoFastDelivery.domain.product.repository;

import nbcamp.TwoFastDelivery.domain.product.entity.ProductOption;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;
import java.util.List;
import java.util.Optional;

public interface ProductOptionRepository extends JpaRepository<ProductOption, UUID> {
    List<ProductOption> findByGroupIdAndDeletedAtIsNull(UUID groupId);

    Optional<ProductOption> findByIdAndDeletedAtIsNull(UUID id);

    boolean existsByGroupIdAndDeletedAtIsNullAndIdNot(UUID groupId, UUID id);
}
