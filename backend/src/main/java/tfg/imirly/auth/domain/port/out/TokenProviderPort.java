package tfg.imirly.auth.domain.port.out;

import tfg.imirly.auth.domain.model.User;

public interface TokenProviderPort {
    String generateToken(User user);
}