package nbcamp.TwoFastDelivery.auth.presentation;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import nbcamp.TwoFastDelivery.auth.application.AuthService;
import nbcamp.TwoFastDelivery.auth.application.TokenDto;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/token")
    public AuthResponseDto.Token getToken(@RequestBody @Valid AuthRequestDto
            .Token request) {
        TokenDto tokenDto = authService.authenticate(request.getUsername(),
                request.getPassword());
        return AuthResponseDto.Token.builder()
                .token(tokenDto.token())
                .build();
    }
}
