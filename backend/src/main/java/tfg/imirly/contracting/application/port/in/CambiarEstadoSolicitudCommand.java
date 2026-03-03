package tfg.imirly.contracting.application.port.in;

import lombok.AllArgsConstructor;
import lombok.Getter;
import tfg.imirly.contracting.domain.model.EstadoSolicitud;
import java.util.UUID;

@Getter
@AllArgsConstructor
public class CambiarEstadoSolicitudCommand {
    private UUID solicitudId;
    private EstadoSolicitud nuevoEstado;
    private UUID usuarioId; // Para validar quién intenta cambiar el estado
}