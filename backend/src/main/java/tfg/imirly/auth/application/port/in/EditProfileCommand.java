package tfg.imirly.auth.application.port.in;

import lombok.AllArgsConstructor;
import lombok.Getter;
import java.util.UUID;

@Getter
@AllArgsConstructor
public class EditProfileCommand {
    private UUID userId;
    private String nombre;
    private String documentoIdentidad;
    private String email; // NUEVO
}