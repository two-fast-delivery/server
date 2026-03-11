package nbcamp.TwoFastDelivery.domain.order.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.UUID;

public class AuthInterceptor implements HandlerInterceptor {

   // private final JwtUtil jwtUtil;

    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
    //    String Token = request.getHeader("Authorization");

    //    if(Token != null && Token.startsWith("Bearer ")) {
    //    String jwt = Token.substring(7);

    //        UUID storeId = jwtUtil.getStoreIdFromToken(jwt);

    //    request.setAttribute("storeId", storeId);
            return true;
    //    }

    //    response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "인증이 필요합니다.");
    //    return false;
    }
}
