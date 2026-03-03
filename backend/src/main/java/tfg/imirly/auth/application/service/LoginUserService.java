package tfg.imirly.auth.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tfg.imirly.auth.application.port.in.LoginUserCommand;
import tfg.imirly.auth.application.port.in.LoginUserUseCase;
import tfg.imirly.auth.domain.model.User;
import tfg.imirly.auth.domain.port.out.PasswordEncoderPort;
import tfg.imirly.auth.domain.port.out.TokenProviderPort;
import tfg.imirly.auth.domain.port.out.UserRepositoryPort;

@Service
@RequiredArgsConstructor
public class LoginUserService implements LoginUserUseCase {

    private final UserRepositoryPort userRepositoryPort;
    private final PasswordEncoderPort passwordEncoderPort;
    private final TokenProviderPort tokenProviderPort;

    @Override
    public String login(LoginUserCommand command) {
        // 1. Buscar al usuario por email
        User user = userRepositoryPort.findByEmail(command.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("Credenciales inválidas"));

        // 2. Comprobar que la contraseña coincide
        if (!passwordEncoderPort.matches(command.getRawPassword(), user.getPasswordHash())) {
            throw new IllegalArgumentException("Credenciales inválidas");
        }

        // 3. Generar y devolver el pase VIP (Token JWT)
        return tokenProviderPort.generateToken(user);
    }
}