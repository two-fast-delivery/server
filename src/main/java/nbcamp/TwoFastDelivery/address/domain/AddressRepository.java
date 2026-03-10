package nbcamp.TwoFastDelivery.address.domain;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AddressRepository extends JpaRepository<Address, AddressId> {

    Page<Address> findAllByCreatedBy(UUID userId, Pageable pageable);
}
