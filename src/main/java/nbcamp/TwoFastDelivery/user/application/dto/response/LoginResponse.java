package nbcamp.TwoFastDelivery.domain.user.application.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter // getter 생성
@AllArgsConstructor // 전체 필드 생성자 생성
public class LoginResponse {

    private String accessToken; // 발급된 JWT access token
    private String userId; // 로그인한 사용자 id
    private String role; // 로그인한 사용자 권한
}