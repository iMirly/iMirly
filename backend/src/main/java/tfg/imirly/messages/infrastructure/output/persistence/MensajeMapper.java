package tfg.imirly.messages.infrastructure.output.persistence;

import org.springframework.stereotype.Component;
import tfg.imirly.auth.infrastructure.output.persistence.entity.UserEntity;
import tfg.imirly.messages.domain.model.Mensaje;
import tfg.imirly.messages.infrastructure.output.persistence.entity.MensajeEntity;

@Component
public class MensajeMapper {

    public Mensaje toDomain(MensajeEntity entity) {
        // El orden debe coincidir con el constructor de tu clase Mensaje:
        // id, remitenteId, receptorId, solicitudId, contenido, tipo, timestamp, leido
        return new Mensaje(
                entity.getId(),
                entity.getRemitente().getId(),
                entity.getReceptor().getId(),
                entity.getSolicitudId(), // <--- NUEVO
                entity.getContenido(),
                entity.getTipo(),        // <--- NUEVO
                entity.getTimestamp(),
                entity.isLeido()
        );
    }

    public MensajeEntity toEntity(Mensaje domain) {
        MensajeEntity entity = new MensajeEntity();
        
        if (domain.getId() != null) {
            entity.setId(domain.getId());
        }
        
        entity.setSolicitudId(domain.getSolicitudId()); // <--- NUEVO
        entity.setContenido(domain.getContenido());
        entity.setTipo(domain.getTipo());               // <--- NUEVO
        entity.setTimestamp(domain.getTimestamp());
        entity.setLeido(domain.isLeido());

        // Vinculamos los usuarios solo por su ID (Truco JPA para claves foráneas)
        UserEntity remitente = new UserEntity();
        remitente.setId(domain.getRemitenteId());
        entity.setRemitente(remitente);

        UserEntity receptor = new UserEntity();
        receptor.setId(domain.getReceptorId());
        entity.setReceptor(receptor);

        return entity;
    }
}