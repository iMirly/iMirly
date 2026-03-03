package tfg.imirly.auth.infrastructure.input.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import java.util.UUID;

@Getter
@AllArgsConstructor
public class UserProfileResponse {
    private UUID id;
    private String nombre;
    private String email;
    private String documentoIdentidad;
    private Double saldo;
    private Double valoracionMedia;
    private Integer numeroValoraciones;
}