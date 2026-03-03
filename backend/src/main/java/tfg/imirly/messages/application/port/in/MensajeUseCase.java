package tfg.imirly.messages.application.port.in;

import tfg.imirly.messages.domain.model.Mensaje;

import java.util.List;
import java.util.UUID;

public interface MensajeUseCase {

    // Acción 1: Enviar un nuevo mensaje
    Mensaje enviarMensaje(EnviarMensajeCommand command);

    // Acción 2: Cargar un chat entero entre dos personas
    List<Mensaje> obtenerConversacion(UUID usuarioActualId, UUID otroUsuarioId);

    // Acción 3: Borrar una conversación
    void borrarConversacion(UUID usuarioActualId, UUID otroUsuarioId);
}