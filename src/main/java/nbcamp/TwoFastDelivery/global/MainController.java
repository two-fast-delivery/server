package nbcamp.TwoFastDelivery.global;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Slf4j
@Controller
public class MainController {

    @GetMapping("/")
    public String index() {
        return "redirect:/api-docs.html";
    }

    @ResponseBody
    @GetMapping("/test123")
    public void test(@AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) {
            log.info("UserDetails is null : 미로그인");
        } else {
            log.info("UserDetails is : {}", userDetails.getUsername());
        }
    }

    @ResponseBody
    @GetMapping("/test")
    public void test2() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && authentication.getPrincipal() instanceof UserDetails) {
            log.info("UserDetails is : {}", ((UserDetails) authentication.getPrincipal()).getUsername());
        } else {
            log.info("UserDetails is null : 미로그인");
        }
    }
}
