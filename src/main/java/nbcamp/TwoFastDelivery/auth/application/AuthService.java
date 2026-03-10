package nbcamp.TwoFastDelivery.auth.application;

public interface AuthService {
    TokenDto authenticate(String username, String password);
}
