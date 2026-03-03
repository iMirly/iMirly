package tfg.imirly.auth.application.port.in;

import tfg.imirly.auth.domain.model.User;

public interface GetUserUseCase {
    User getUserByEmail(String email);
}