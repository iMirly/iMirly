package tfg.imirly.contracting.infrastructure.output.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tfg.imirly.contracting.infrastructure.output.persistence.entity.SolicitudContratoEntity;

import java.util.List;
import java.util.UUID;

public interface SolicitudContratoRepository extends JpaRepository<SolicitudContratoEntity, UUID> {

    // Encuentra todas las solicitudes que ha hecho un cliente
    List<SolicitudContratoEntity> findByClienteId(UUID clienteId);

    // Encuentra todas las solicitudes que han recibido los anuncios de un
    // profesional
    List<SolicitudContratoEntity> findByAnuncioProveedorId(UUID proveedorId);

    @org.springframework.data.jpa.repository.Modifying
    @org.springframework.data.jpa.repository.Query("DELETE FROM SolicitudContratoEntity s WHERE (s.cliente.id = :usuario1 AND s.anuncio.proveedor.id = :usuario2) OR (s.cliente.id = :usuario2 AND s.anuncio.proveedor.id = :usuario1)")
    void borrarSolicitudesEntreUsuarios(@org.springframework.data.repository.query.Param("usuario1") UUID usuario1,
            @org.springframework.data.repository.query.Param("usuario2") UUID usuario2);
}