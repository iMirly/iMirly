package tfg.imirly.auth.infrastructure.input.rest.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EditProfileRequest {
    private String nombre;
    private String documentoIdentidad;
    private String email; // NUEVO
}