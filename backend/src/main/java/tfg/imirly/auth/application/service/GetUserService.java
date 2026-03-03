package tfg.imirly.auth.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tfg.imirly.auth.application.port.in.GetUserUseCase;
import tfg.imirly.auth.domain.model.User;
import tfg.imirly.auth.domain.port.out.UserRepositoryPort;

@Service
@RequiredArgsConstructor
public class GetUserService implements GetUserUseCase {

    private final UserRepositoryPort userRepositoryPort;

    @Override
    public User getUserByEmail(String email) {
        // Usamos el método que ya tenías en el puerto
        return userRepositoryPort.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));
    }
}