package tfg.imirly.auth.infrastructure.input.rest.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterUserRequest {
    private String nombre;
    private String email;
    private String rawPassword;
    private String tipoUsuario;
    private String documentoIdentidad;
}