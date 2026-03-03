package tfg.imirly.contracting.infrastructure.input.rest.dto;

import lombok.Getter;
import lombok.Setter;
import java.util.UUID;

@Getter
@Setter
public class CrearSolicitudRequest {
    private UUID anuncioId;
    private String detalles;
}