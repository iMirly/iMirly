package tfg.imirly.messages.application.port.in;

import lombok.AllArgsConstructor;
import lombok.Getter;
import tfg.imirly.messages.domain.model.TipoMensaje;
import java.util.UUID;

@Getter
@AllArgsConstructor
public class EnviarMensajeCommand {
    private UUID remitenteId;
    private UUID receptorId;
    private UUID solicitudId;
    private String contenido;
    private TipoMensaje tipo;
}