package nbcamp.TwoFastDelivery.address.domain;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepository extends JpaRepository<Address, AddressId> {
}
