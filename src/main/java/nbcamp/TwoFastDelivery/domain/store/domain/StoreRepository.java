package nbcamp.TwoFastDelivery.domain.store.domain;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;

public interface StoreRepository extends JpaRepository<Store, UUID> {

    Optional<Store> findByIdAndDeletedAtIsNull(UUID id);

    List<Store> findByUserIdAndDeletedAtIsNull(UUID userId);

    Page<Store> findByStatusAndDeletedAtIsNull(StoreStatus status, org.springframework.data.domain.Pageable pageable);
}
