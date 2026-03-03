package tfg.imirly.auth.application.port.in;

import lombok.AllArgsConstructor;
import lombok.Getter;
import tfg.imirly.auth.domain.model.UserType;

@Getter
@AllArgsConstructor
public class RegisterUserCommand {
    private String nombre;
    private String email;
    private String rawPassword; // Contraseña sin cifrar
    private UserType tipoUsuario;
    private String documentoIdentidad;
}