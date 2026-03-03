package tfg.imirly.auth.application.service;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import tfg.imirly.auth.application.port.in.RegisterUserCommand;
import tfg.imirly.auth.application.port.in.RegisterUserUseCase;
import tfg.imirly.auth.domain.model.User;
import tfg.imirly.auth.domain.port.out.PasswordEncoderPort;
import tfg.imirly.auth.domain.port.out.UserRepositoryPort;

@Service
@RequiredArgsConstructor
public class RegisterUserService implements RegisterUserUseCase {

    // Dependencias inyectadas mediante el constructor (gracias a @RequiredArgsConstructor)
    private final UserRepositoryPort userRepositoryPort;
    private final PasswordEncoderPort passwordEncoderPort;

    @Override
    public User registerUser(RegisterUserCommand command) {
        // 1. Regla de negocio: El email no puede estar repetido
        if (userRepositoryPort.existsByEmail(command.getEmail())) {
            throw new IllegalArgumentException("El email ya está registrado en iMirly");
        }

        // 2. Cifrar la contraseña
        String encodedPassword = passwordEncoderPort.encode(command.getRawPassword());

        // 3. Crear el objeto de Dominio
        User newUser = new User(
                command.getNombre(),
                command.getEmail(),
                encodedPassword,
                command.getTipoUsuario(),
                command.getDocumentoIdentidad()
        );

        // 4. Guardar usando el puerto de salida y devolver el resultado
        return userRepositoryPort.save(newUser);
    }
}