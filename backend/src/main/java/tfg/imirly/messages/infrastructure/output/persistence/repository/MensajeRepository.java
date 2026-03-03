package tfg.imirly.messages.infrastructure.output.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import tfg.imirly.messages.infrastructure.output.persistence.entity.MensajeEntity;

import java.util.List;
import java.util.UUID;

public interface MensajeRepository extends JpaRepository<MensajeEntity, UUID> {

    // 1. OBTENER LA CONVERSACIÓN ENTRE DOS USUARIOS
    @Query("SELECT m FROM MensajeEntity m WHERE " +
            "(m.remitente.id = :usuario1 AND m.receptor.id = :usuario2) OR " +
            "(m.remitente.id = :usuario2 AND m.receptor.id = :usuario1) " +
            "ORDER BY m.timestamp ASC")
    List<MensajeEntity> findConversacion(@Param("usuario1") UUID usuario1, @Param("usuario2") UUID usuario2);

    // 2. MARCAR MENSAJES COMO LEÍDOS
    // Usamos @Modifying porque es una consulta de actualización (UPDATE), no de
    // lectura (SELECT)
    @Modifying
    @Query("UPDATE MensajeEntity m SET m.leido = true WHERE m.remitente.id = :remitenteId AND m.receptor.id = :receptorId AND m.leido = false")
    void marcarMensajesComoLeidos(@Param("remitenteId") UUID remitenteId, @Param("receptorId") UUID receptorId);

    @Modifying
    @Query("DELETE FROM MensajeEntity m WHERE (m.remitente.id = :usuario1 AND m.receptor.id = :usuario2) OR (m.remitente.id = :usuario2 AND m.receptor.id = :usuario1)")
    void borrarConversacion(@Param("usuario1") UUID usuario1, @Param("usuario2") UUID usuario2);
}