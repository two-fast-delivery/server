package nbcamp.TwoFastDelivery.auth.application;

import lombok.extern.slf4j.Slf4j;
import nbcamp.TwoFastDelivery.user.application.dto.request.CreateUserRequest;
import nbcamp.TwoFastDelivery.user.application.dto.response.UserResponse;
import nbcamp.TwoFastDelivery.user.application.service.UserService;
import nbcamp.TwoFastDelivery.user.domain.user.User;
import nbcamp.TwoFastDelivery.user.domain.user.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Slf4j
@Transactional
@SpringBootTest
@ActiveProfiles("dev")
public class TokenServiceTest {
    @Autowired
    TokenService tokenService;

    @Autowired
    UserService userService;

    @Autowired
    UserRepository userRepository;

    UUID userId;

    @BeforeEach
    void setup() {
        CreateUserRequest request = CreateUserRequest
                .builder()
                .email("user01@test.org")
                .password("12345678")
                .username("user01")
                .nickname("사용자01")
                .build();

        UserResponse res = userService.createUser(request);
        log.info("UserResponse: {}", res);

        User user = userRepository.findByEmail(request.getEmail()).orElse(null);
        this.userId = user.getUserId().getId();
    }

    @Test
    void tokenCreateTest() {
        TokenDto tokenDto = tokenService.create(userId);
        log.info("tokenDto: {}", tokenDto);
    }
}