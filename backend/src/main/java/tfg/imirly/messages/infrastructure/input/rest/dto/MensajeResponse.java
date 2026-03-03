package tfg.imirly.messages.infrastructure.input.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import tfg.imirly.messages.domain.model.TipoMensaje;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@AllArgsConstructor
public class MensajeResponse {
    private UUID id;
    private UUID remitenteId;
    private UUID receptorId;
    private UUID solicitudId; // <--- NUEVO
    private String contenido;
    private TipoMensaje tipo; // <--- NUEVO
    private LocalDateTime timestamp;
    private boolean leido;
}