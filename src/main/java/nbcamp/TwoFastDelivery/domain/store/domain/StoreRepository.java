package nbcamp.TwoFastDelivery.domain.store.domain;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.domain.Page;

public interface StoreRepository extends JpaRepository<Store, UUID> {

    Optional<Store> findByIdAndDeletedAtIsNull(UUID id);

    List<Store> findByUserIdAndDeletedAtIsNull(UUID userId);

    Page<Store> findByStatusAndDeletedAtIsNull(StoreStatus status, org.springframework.data.domain.Pageable pageable);

    @Query("SELECT s FROM Store s " +
        "WHERE s.status = :status AND " +
        "s.deletedAt IS NULL AND " +
        "(:categoryId IS NULL OR s.category_id = :categoryId) AND " +
        "(:keyword IS NULL OR :keyword = '' OR lower(s.name) LIKE lower(concat('%', :keyword, '%')))")
    Page<Store> searchStores(@Param("status")StoreStatus status, @Param("categoryId")UUID categoryId, @Param("keyword")String keyword, Pageable pageable);
}
