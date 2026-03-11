package nbcamp.TwoFastDelivery.domain.store.domain;

import java.util.UUID;
import jakarta.persistence.*;
import lombok.Getter;






@Entity
@Getter
@Table(name = "p_category")
public class Category {
    @Id
    @Column(name = "id", nullable = false, columnDefinition = "uuid")
    private UUID id;

    @Column(name = "name", nullable = false, columnDefinition = "varchar(50)")
    private String name;
}