package nbcamp.TwoFastDelivery.domain.store.domain;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

public interface StoreRequestRepository extends JpaRepository<StoreRequest, UUID> {

    List<StoreRequest> findByStore_IdAndStatus(UUID storeId, StoreRequestStatus status);
    
}
