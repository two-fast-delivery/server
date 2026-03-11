package nbcamp.TwoFastDelivery.auth.infrastructure;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import nbcamp.TwoFastDelivery.auth.application.TokenService;
import nbcamp.TwoFastDelivery.global.exception.CustomException;
import nbcamp.TwoFastDelivery.global.exception.ErrorCode;
import nbcamp.TwoFastDelivery.user.domain.user.User;
import nbcamp.TwoFastDelivery.user.domain.user.UserId;
import nbcamp.TwoFastDelivery.user.domain.user.UserRepository;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class JwtFilter extends GenericFilterBean {
    private final TokenService tokenService;
    private final UserRepository userRepository;

    /**
     *
     * 요청 헤더
     *      Authorization: Basic 아이디 비밀번호 인증
     *      Authorization: Bearer 토큰(JWT)
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        String token = getToken((HttpServletRequest) request);
        try {
            if (StringUtils.hasText(token)) {
                UUID userId = tokenService.getUserId(token);
                if (userId == null) {
                    throw new CustomException(ErrorCode.UNAUTHORIZED);
                }

                // 회원정보 추출 -> 스프링 시큐리티에 로그인 처리
                User user = userRepository.findById(UserId.of(userId)).orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND));

                UserDetails userDetails = org.springframework.security.core.userdetails.User.builder()
                        .username(user.getUsername())
                        .password("")
                        .authorities(List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole().name())))
                        .build();

                Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

                SecurityContextHolder.getContext().setAuthentication(authentication); // 로그인 처리
            }
        } catch (CustomException e) {
            HttpServletResponse res = (HttpServletResponse) response;
            res.sendError(e.getErrorCode().getStatus().value(), e.getMessage());
        }
        chain.doFilter(request, response);
    }

    private String getToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (StringUtils.hasText(bearerToken) && bearerToken.toUpperCase().startsWith("BEARER ")) {
            return bearerToken.substring(7).trim();
        }

        return null;
    }
}