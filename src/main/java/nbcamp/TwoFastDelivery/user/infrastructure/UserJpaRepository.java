package nbcamp.TwoFastDelivery.user.infrastructure;

import nbcamp.TwoFastDelivery.user.domain.User;
import nbcamp.TwoFastDelivery.user.domain.UserId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserJpaRepository extends JpaRepository<User, UserId> {
    boolean existsByEmail(String email);
    Optional<User> findByEmail(String email);
}
