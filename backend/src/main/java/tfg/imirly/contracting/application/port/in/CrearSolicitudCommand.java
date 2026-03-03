package tfg.imirly.contracting.application.port.in;

import lombok.AllArgsConstructor;
import lombok.Getter;
import java.util.UUID;

@Getter
@AllArgsConstructor
public class CrearSolicitudCommand {
    private UUID clienteId;
    private UUID anuncioId;
    private String detalles;
}