package nbcamp.TwoFastDelivery.auth.infrastructure;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import nbcamp.TwoFastDelivery.auth.application.TokenDto;
import nbcamp.TwoFastDelivery.auth.application.TokenService;
import nbcamp.TwoFastDelivery.global.exception.CustomException;
import nbcamp.TwoFastDelivery.global.exception.ErrorCode;
import nbcamp.TwoFastDelivery.user.domain.user.User;
import nbcamp.TwoFastDelivery.user.domain.user.UserId;
import nbcamp.TwoFastDelivery.user.domain.user.UserRepository;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.security.Key;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.UUID;

@Slf4j
@Service
@EnableConfigurationProperties(JwtProperties.class)
public class TokenServiceImpl implements TokenService {

    private final Key key; // Signature 생성시 활용
    private final UserRepository userRepository;
    private final JwtProperties properties;

    public TokenServiceImpl(JwtProperties properties, UserRepository userRepository) {
        log.info("JwtProperties: {}", properties);
        byte[] keyBytes = Decoders.BASE64.decode(properties.secret());
        this.key = Keys.hmacShaKeyFor(keyBytes);

        this.properties = properties;
        this.userRepository = userRepository;
    }

    @Override
    public TokenDto create(UUID userId) {

        User user = userRepository.findById(UserId.of(userId)).orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND));

        // 토큰 유효시간
        Date now = new Date();
        Date expired = new Date(now.getTime() + properties.validTime() * 1000);

        String token =  Jwts.builder()
                .setSubject(userId.toString())
                .signWith(key, SignatureAlgorithm.HS512)
                .setExpiration(expired)
                .compact();

        return new TokenDto(token, LocalDateTime.ofInstant(expired.toInstant(), ZoneId.systemDefault()));
    }

    @Override
    public void validate(String token) {
        String message = null;
        Exception error = null;
        try {
            Jwts.parser().setSigningKey(key).parseClaimsJws(token).getBody();

        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            message = "잘못된 JWT 서명입니다.";
            error = e;
        } catch (ExpiredJwtException e) {
            message = "만료된 JWT 토큰입니다.";
            error = e;
        } catch (UnsupportedJwtException e) {
            message = "지원되지 않는 JWT 토큰 입니다.";
            error = e;
        } catch (IllegalArgumentException e) {
            message = "JWT 토큰이 잘못되었습니다.";
            error = e;
        }
        if (error != null) {
            log.error(message, error);
            throw new CustomException(ErrorCode.UNAUTHORIZED);
        }
    }

    @Override
    public UUID getUserId(String token) {
        if (StringUtils.hasText(token)) {
            validate(token); // 토큰 유효성 검사

            Claims claims = Jwts.parser().setSigningKey(key).parseClaimsJws(token).getBody();
            return UUID.fromString(claims.getSubject());
        }
        return null;
    }
}