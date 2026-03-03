package tfg.imirly.messages.domain.port.out;

import tfg.imirly.messages.domain.model.Mensaje;

import java.util.List;
import java.util.UUID;

public interface MensajeRepositoryPort {

    // Guardar un mensaje nuevo
    Mensaje save(Mensaje mensaje);

    // Recuperar el historial de chat entre el usuario A y el usuario B
    List<Mensaje> findConversacion(UUID usuario1Id, UUID usuario2Id);

    // Cambiar el estado a leído de todos los mensajes que el 'remitente' le mandó
    // al 'receptor'
    void marcarMensajesComoLeidos(UUID remitenteId, UUID receptorId);

    // Borrar el historial de chat entre el usuario A y el usuario B
    void borrarConversacion(UUID usuario1Id, UUID usuario2Id);
}