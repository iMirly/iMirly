package tfg.imirly.auth.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import tfg.imirly.auth.application.port.in.ChangePasswordCommand;
import tfg.imirly.auth.application.port.in.ChangePasswordUseCase;
import tfg.imirly.auth.domain.model.User;
import tfg.imirly.auth.domain.port.out.UserRepositoryPort;

@Service
@RequiredArgsConstructor
public class ChangePasswordService implements ChangePasswordUseCase {

    private final UserRepositoryPort userRepositoryPort;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void changePassword(ChangePasswordCommand command) {
        // 1. Buscamos al usuario por el email (que viene seguro desde el Token)
        User user = userRepositoryPort.findByEmail(command.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

        // 2. Comprobamos si la contraseña antigua que ha escrito coincide con la real
        if (!passwordEncoder.matches(command.getOldPassword(), user.getPasswordHash())) {
            throw new IllegalArgumentException("La contraseña actual es incorrecta");
        }

        // 3. Si es correcta, encriptamos la nueva y la guardamos
        user.changePassword(passwordEncoder.encode(command.getNewPassword()));
        userRepositoryPort.save(user);
    }
}