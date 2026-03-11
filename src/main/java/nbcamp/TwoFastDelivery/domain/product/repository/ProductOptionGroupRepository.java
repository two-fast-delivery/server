package nbcamp.TwoFastDelivery.domain.product.repository;

import nbcamp.TwoFastDelivery.domain.product.entity.ProductOptionGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;
import java.util.List;
import java.util.Optional;

public interface ProductOptionGroupRepository extends JpaRepository<ProductOptionGroup, UUID> {
    List<ProductOptionGroup> findByProductIdAndDeletedAtIsNull(UUID productId);

    Optional<ProductOptionGroup> findByIdAndDeletedAtIsNull(UUID id);

    Optional<ProductOptionGroup> findByProductIdAndNameAndDeletedAtIsNull(UUID productId, String name);
}
