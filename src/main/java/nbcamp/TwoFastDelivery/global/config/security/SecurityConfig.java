package nbcamp.TwoFastDelivery.global.config.security;

import lombok.RequiredArgsConstructor;
import nbcamp.TwoFastDelivery.auth.infrastructure.JwtFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

/**
 * 메서드 단위에서 인가 처리
 * @EnableMethodSecurity
 *  -> @PreAuthorize
 * @PreAuthorize("hasAnyRole('OWNER',..)")
 *  -> @PostAuthorize
 *  -> @Secure
 */

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final JwtFilter jwtFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(c -> c.configurationSource(corsConfigurationSource()))
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .csrf(csrf -> csrf.disable())
                .formLogin(form -> form.disable())
                .httpBasic(basic -> basic.disable())
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers("/api/users/**").permitAll()
                        .requestMatchers("/", "/v3/api-docs/**", "/api-docs/**", "/api-docs.html", "/swagger-ui/**").permitAll()

                        // 공개 조회
                        .requestMatchers("/reviews/stores/**").permitAll()
                        .requestMatchers(org.springframework.http.HttpMethod.GET, "/reviews/*").permitAll()

                        // 인증 필요
                        .requestMatchers("/reviews/me").authenticated()
                        .requestMatchers(org.springframework.http.HttpMethod.POST, "/reviews").authenticated()
                        .requestMatchers(org.springframework.http.HttpMethod.PATCH, "/reviews/*").authenticated()
                        .requestMatchers(org.springframework.http.HttpMethod.DELETE, "/reviews/*").authenticated()

                        .anyRequest().permitAll()
                )
                .exceptionHandling(c -> {
                    c.authenticationEntryPoint((req, resp, e) -> {
                        resp.sendError(HttpStatus.UNAUTHORIZED.value(), e.getMessage());
                    });

                    c.accessDeniedHandler((req, resp, e) -> {
                        resp.sendError(HttpStatus.FORBIDDEN.value(), e.getMessage());
                    });
                });

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedHeaders(List.of("*"));
        config.setAllowedMethods(List.of("*"));
        config.setAllowedOrigins(List.of("*"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);

        return source;
    }
}