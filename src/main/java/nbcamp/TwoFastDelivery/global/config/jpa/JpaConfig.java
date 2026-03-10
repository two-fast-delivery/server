package nbcamp.TwoFastDelivery.global.config.jpa;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.util.Optional;

import java.util.UUID;

/**
 * JPA Auditing 활성화 설정
 * - @CreatedDate, @LastModifiedDate 자동 주입
 * - @CreatedBy, @LastModifiedBy 사용 시 AuditorAware 필요
 */
@Configuration
@EnableJpaAuditing
public class JpaConfig {

    /**
     * JWT/시큐리티 적용 전 Optional.empty()
     * JWT 붙이면 "현재 로그인 유저 ID"를 꺼내서 return
     */
    @Bean
    public AuditorAware<UUID> auditorAware() {
        return () -> Optional.empty();
    }
}
