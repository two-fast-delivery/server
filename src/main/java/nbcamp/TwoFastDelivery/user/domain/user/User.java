package nbcamp.TwoFastDelivery.user.domain.user;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "p_user")
public class User {

    @EmbeddedId
    private UserId userId;

    @Column(nullable = false, unique = true, length = 100)
    private String username;

    @Column(nullable = false, unique = true, length = 225)
    private String email;

    @Column(nullable = false, length = 100)
    private String password;

    @Column(nullable = false, length = 100)
    private String nickname;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRole role;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserStatus status;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    public static User create(String username, String email, String nickname, String password) {
        User user = new User();

        user.userId = UserId.of(UUID.randomUUID());
        user.username = username;
        user.email = email;
        user.nickname = nickname;
        user.password = password;

        user.role = UserRole.CUSTOMER; // 기본 세팅값
        user.status = UserStatus.ACTIVE; // 기본 세팅값

        user.createdAt = LocalDateTime.now();
        user.updatedAt = LocalDateTime.now();

        return user;
    }

    public void update(String nickname, String password){
        this.nickname = nickname;
        this.password = password;
        this.updatedAt = java.time.LocalDateTime.now();
    }

    public void updateStatus(UserStatus status) {
        this.status = status;
        this.updatedAt = java.time.LocalDateTime.now();
    }

    public void changeRole(UserRole role) {
        this.role = role;
    }
}


