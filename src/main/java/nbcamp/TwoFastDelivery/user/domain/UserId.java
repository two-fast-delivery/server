package nbcamp.TwoFastDelivery.user.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
<<<<<<< HEAD
import jakarta.persistence.EmbeddedId;
=======
>>>>>>> 081a7bc2d0ec7ca011b0c4eedbdc81a57a86c25f
import lombok.*;

import java.io.Serializable;
import java.util.UUID;

@ToString
@Getter
@Embeddable
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserId implements Serializable {
    @Column(length=45, name="user_id")
    private UUID id;

    public UserId(UUID id) { this.id = id;}

<<<<<<< HEAD
    public static UserId of(UUID id) {return new UserId(id);}
=======
    public static UserId of(UUID id) { return new UserId(id);}
>>>>>>> 081a7bc2d0ec7ca011b0c4eedbdc81a57a86c25f
}
