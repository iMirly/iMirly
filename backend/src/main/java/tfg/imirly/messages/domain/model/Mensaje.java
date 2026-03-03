package tfg.imirly.messages.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Mensaje {
    
    private UUID id;
    private UUID remitenteId;
    private UUID receptorId;
    private UUID solicitudId; // <--- Vínculo con la negociación/contrato
    private String contenido;
    private TipoMensaje tipo; // <--- TEXTO, PROPUESTA_PRECIO, etc.
    private LocalDateTime timestamp;
    private boolean leido;

    // Constructor para crear mensajes nuevos con el nuevo flujo
    public Mensaje(UUID remitenteId, UUID receptorId, UUID solicitudId, String contenido, TipoMensaje tipo) {
        this.remitenteId = remitenteId;
        this.receptorId = receptorId;
        this.solicitudId = solicitudId;
        this.contenido = contenido;
        this.tipo = tipo;
        
        // Reglas de negocio automáticas
        this.timestamp = LocalDateTime.now();
        this.leido = false; 
    }

    // Regla de negocio: marcar un mensaje como leído
    public void marcarComoLeido() {
        if (!this.leido) {
            this.leido = true;
        }
    }
}