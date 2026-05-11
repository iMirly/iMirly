package tfg.imirly.auth.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tfg.imirly.auth.application.port.in.EditProfileCommand;
import tfg.imirly.auth.application.port.in.EditProfileUseCase;
import tfg.imirly.auth.domain.model.User;
import tfg.imirly.auth.domain.port.out.UserRepositoryPort;
import tfg.imirly.messages.application.service.ProfanityFilterService;

@Service
@RequiredArgsConstructor
public class EditProfileService implements EditProfileUseCase {

    private final UserRepositoryPort userRepositoryPort;
    private final ProfanityFilterService profanityFilterService;

   @Override
    public User editProfile(EditProfileCommand command) {
        User user = userRepositoryPort.findById(command.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

        // 1. Validar que el nuevo nombre no contenga insultos
        if (command.getNombre() != null && profanityFilterService.containsProfanity(command.getNombre())) {
            throw new IllegalArgumentException("El nombre contiene lenguaje no permitido.");
        }

        // 2. Verificamos si quiere cambiar el email y si está libre
        if (command.getEmail() != null && !command.getEmail().isBlank() && !command.getEmail().equals(user.getEmail())) {
            if (userRepositoryPort.existsByEmail(command.getEmail())) {
                throw new IllegalArgumentException("Ese correo electrónico ya está en uso");
            }
        }

        user.updateProfile(command.getNombre(), command.getDocumentoIdentidad(), command.getEmail());
        return userRepositoryPort.save(user);
    }
}
