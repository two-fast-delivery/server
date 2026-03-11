package nbcamp.TwoFastDelivery.auth.presentation;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import nbcamp.TwoFastDelivery.auth.application.TokenDto;
import nbcamp.TwoFastDelivery.auth.application.TokenService;
import nbcamp.TwoFastDelivery.user.application.dto.request.CreateUserRequest;
import nbcamp.TwoFastDelivery.user.application.dto.response.UserResponse;
import nbcamp.TwoFastDelivery.user.application.service.UserService;
import nbcamp.TwoFastDelivery.user.domain.user.User;
import nbcamp.TwoFastDelivery.user.domain.user.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@Slf4j
@Transactional
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("dev")
public class AuthControllerTest {

    @Autowired
    TokenService tokenService;

    @Autowired
    UserService userService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    MockMvc mockMvc;

    CreateUserRequest request;

    String token;

    @BeforeEach
    void setup() {
        request = CreateUserRequest
                .builder()
                .email("user01@test.org")
                .password("12345678")
                .username("user01")
                .nickname("사용자01")
                .build();

        UserResponse res = userService.createUser(request);
        log.info("UserResponse: {}", res);

        User user = userRepository.findByEmail(request.getEmail()).orElse(null);
        UUID userId = user.getUserId().getId();

        TokenDto tokenDto = tokenService.create(userId);
        this.token = tokenDto.token();
    }

    @Test
    void tokenAuthTest() throws Exception {
        mockMvc.perform(get("/test")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
                .andDo(print());
    }

    @Test
    void tokenEndpointTest() throws Exception {
        AuthRequestDto.Token tokenRequest = new AuthRequestDto.Token();
        tokenRequest.setUsername(request.getUsername());
        tokenRequest.setPassword(request.getPassword());

        ObjectMapper om = new ObjectMapper();

        String body = om.writeValueAsString(tokenRequest);

        mockMvc.perform(post("/api/auth/token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andDo(print());
    }
}