package tfg.imirly.contracting.domain.port.out;

import tfg.imirly.contracting.domain.model.SolicitudContrato;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SolicitudContratoRepositoryPort {
    SolicitudContrato save(SolicitudContrato solicitud);

    Optional<SolicitudContrato> findById(UUID id);

    List<SolicitudContrato> findByClienteId(UUID clienteId);

    // 👇 ESTE ES EL MÉTODO QUE FALTABA PARA QUE EL SERVICE COMPILE 👇
    List<SolicitudContrato> findByProveedorId(UUID proveedorId);

    // Método para borrar permanentemente
    void deleteById(UUID id);

    // Borrar solicitudes entre dos usuarios
    void borrarSolicitudesEntreUsuarios(UUID usuario1, UUID usuario2);
}