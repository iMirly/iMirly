package tfg.imirly.auth.application.service;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import tfg.imirly.auth.application.port.in.RegisterUserCommand;
import tfg.imirly.auth.application.port.in.RegisterUserUseCase;
import tfg.imirly.auth.domain.model.User;
import tfg.imirly.auth.domain.port.out.PasswordEncoderPort;
import tfg.imirly.auth.domain.port.out.UserRepositoryPort;
import tfg.imirly.messages.application.service.ProfanityFilterService;

@Service
@RequiredArgsConstructor
public class RegisterUserService implements RegisterUserUseCase {

    private final UserRepositoryPort userRepositoryPort;
    private final PasswordEncoderPort passwordEncoderPort;
    private final ProfanityFilterService profanityFilterService;

    @Override
    public User registerUser(RegisterUserCommand command) {
        // 1. Regla de negocio: El email no puede estar repetido
        if (userRepositoryPort.existsByEmail(command.getEmail())) {
            throw new IllegalArgumentException("El email ya está registrado en iMirly");
        }

        // 2. Regla de negocio: El nombre no puede contener insultos
        if (profanityFilterService.containsProfanity(command.getNombre())) {
            throw new IllegalArgumentException("El nombre contiene lenguaje no permitido.");
        }

        // 3. Cifrar la contraseña
        String encodedPassword = passwordEncoderPort.encode(command.getRawPassword());

        // 4. Crear el objeto de Dominio
        User newUser = new User(
                command.getNombre(),
                command.getEmail(),
                encodedPassword,
                command.getTipoUsuario(),
                command.getDocumentoIdentidad()
        );

        // 5. Guardar usando el puerto de salida y devolver el resultado
        return userRepositoryPort.save(newUser);
    }
}
