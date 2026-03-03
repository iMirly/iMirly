package tfg.imirly.auth.infrastructure.output.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import tfg.imirly.auth.domain.port.out.PasswordEncoderPort;

@Component
@RequiredArgsConstructor
public class BasicPasswordEncoderAdapter implements PasswordEncoderPort {

    // Inyectamos el "motor" BCrypt que creamos en SecurityConfig
    private final PasswordEncoder springPasswordEncoder;

    @Override
    public String encode(String rawPassword) {
        // Usamos BCrypt para guardar la contraseña de forma segura (unidireccional)
        return springPasswordEncoder.encode(rawPassword);
    }

    @Override
    public boolean matches(String rawPassword, String encodedPassword) {
        // Usamos BCrypt para comparar de forma segura
        return springPasswordEncoder.matches(rawPassword, encodedPassword);
    }
}