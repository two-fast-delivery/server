package nbcamp.TwoFastDelivery.domain.order.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import nbcamp.TwoFastDelivery.domain.order.dto.OrderSearchCondition;
import nbcamp.TwoFastDelivery.domain.order.entity.Order;
import nbcamp.TwoFastDelivery.domain.order.repository.OrderRepositoryCustom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
public class OrderRepositoryImpl implements OrderRepositoryCustom {
    private final EntityManager em;

    @Override
    public Page<Order> searchOrders(OrderSearchCondition condition, Pageable pageable) {
        // 1. 기본 쿼리 작성 (공백 주의)
        StringBuilder jpql = new StringBuilder("select o from Order o left join fetch o.orderDetails where 1=1 ");

        // 2. 키워드 검색 로직
        if (condition.getKeyword() != null && !condition.getKeyword().isEmpty()) {
            jpql.append("and o.menuName like :keyword ");
        }

        // 3. 기간 필터링 로직 (추가)
        if (condition.getPeriod() != null) {
            jpql.append("and o.createdAt >= :startDate ");
        }

        // 4. 쿼리 생성
        TypedQuery<Order> query = em.createQuery(jpql.toString(), Order.class);

        // 5. 파라미터 바인딩
        if (condition.getKeyword() != null && !condition.getKeyword().isEmpty()) {
            query.setParameter("keyword", "%" + condition.getKeyword() + "%");
        }

        if (condition.getPeriod() != null) {
            query.setParameter("startDate", calculateStartDate(condition.getPeriod()));
        }

        // 6. 페이징 처리
        List<Order> content = query.setFirstResult((int) pageable.getOffset())
                .setMaxResults(pageable.getPageSize())
                .getResultList();

        return new PageImpl<>(content, pageable, 100); // 전체 개수는 실제 count 쿼리로 구해야 함
    }

    // 기간 계산 유틸 메서드
    private LocalDateTime calculateStartDate(String period) {
        LocalDateTime now = LocalDateTime.now();
        return switch (period) {
            case "1d" -> now.minusDays(1);
            case "1w" -> now.minusWeeks(1);
            case "1m" -> now.minusMonths(1);
            case "1y" -> now.minusYears(1);
            default -> now.minusYears(100); // 필터링 안 함
        };
    }
}