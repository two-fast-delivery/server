package nbcamp.TwoFastDelivery.domain.store.domain;

import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Repository;


@Repository
public class CategoryRepository {

    public Optional<Category> findById(UUID categoryId) {
        throw new UnsupportedOperationException("Unimplemented method 'findById'");
    }

}
