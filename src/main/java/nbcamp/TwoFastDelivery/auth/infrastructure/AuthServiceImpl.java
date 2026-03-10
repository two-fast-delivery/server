package nbcamp.TwoFastDelivery.auth.infrastructure;

import lombok.RequiredArgsConstructor;
import nbcamp.TwoFastDelivery.auth.application.AuthService;
import nbcamp.TwoFastDelivery.auth.application.TokenDto;
import nbcamp.TwoFastDelivery.auth.application.TokenService;
import nbcamp.TwoFastDelivery.global.exception.CustomException;
import nbcamp.TwoFastDelivery.global.exception.ErrorCode;
import nbcamp.TwoFastDelivery.user.domain.user.User;
import nbcamp.TwoFastDelivery.user.domain.user.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;
    private final UserRepository userRepository;

    @Override
    public TokenDto authenticate(String username, String password) {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new CustomException(ErrorCode.AUTHENTICATION_ACCOUNT_MISMATCH));

        // 비밀번호 검증
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new CustomException(ErrorCode.AUTHENTICATION_ACCOUNT_MISMATCH);
        }

        // 아이디 & 비밀번호 검증 성공 -> 토큰 발급

        return tokenService.create(user.getUserId().getId());
    }


}