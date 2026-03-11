package nbcamp.TwoFastDelivery.auth.infrastructure;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "jwt")
public record JwtProperties(
        String secret, // jwt.secret
        long validTime // jwt.valid-time
) {}
