package tfg.imirly.messages.infrastructure.input.rest.dto;

import lombok.Getter;
import lombok.Setter;
import tfg.imirly.messages.domain.model.TipoMensaje;

import java.util.UUID;

@Getter
@Setter
public class EnviarMensajeRequest {
    private UUID receptorId;
    private UUID solicitudId;
    private String contenido;
    private TipoMensaje tipo;
}