package nbcamp.TwoFastDelivery.domain.product.repository;

import nbcamp.TwoFastDelivery.domain.product.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;
import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, UUID> {
    List<Product> findByStoreIdAndDeletedAtIsNull(UUID storeId);

    List<Product> findByProductGroupIdAndDeletedAtIsNull(UUID productGroupId);

    boolean existsByProductGroupIdAndDeletedAtIsNullAndIdNot(UUID productGroupId, UUID id);

    Optional<Product> findByIdAndDeletedAtIsNull(UUID id);
}
