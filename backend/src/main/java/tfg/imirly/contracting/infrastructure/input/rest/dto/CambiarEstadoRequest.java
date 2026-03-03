package tfg.imirly.contracting.infrastructure.input.rest.dto;

import lombok.Getter;
import lombok.Setter;
import tfg.imirly.contracting.domain.model.EstadoSolicitud;

@Getter
@Setter
public class CambiarEstadoRequest {
    private EstadoSolicitud nuevoEstado;
}