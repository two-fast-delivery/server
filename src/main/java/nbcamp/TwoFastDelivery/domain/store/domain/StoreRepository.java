package nbcamp.TwoFastDelivery.domain.store.domain;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

public interface StoreRepository extends JpaRepository<Store, UUID> {

    Optional<Store> findByIdAndDeletedAtIsNull(UUID id);

    List<Store> findByUserIdAndDeletedAtIsNull(UUID userId);
}
