package nbcamp.TwoFastDelivery.domain.product.repository;

import nbcamp.TwoFastDelivery.domain.product.entity.ProductGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;
import java.util.List;
import java.util.Optional;

public interface ProductGroupRepository extends JpaRepository<ProductGroup, UUID> {
    List<ProductGroup> findByStoreIdAndDeletedAtIsNull(UUID storeId);

    Optional<ProductGroup> findByIdAndDeletedAtIsNull(UUID id);

    Optional<ProductGroup> findByNameAndStoreIdAndDeletedAtIsNull(String name, UUID storeId);
}
