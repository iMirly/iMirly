package tfg.imirly.contracting.application.port.in;

import tfg.imirly.contracting.domain.model.SolicitudContrato;
import java.util.List;
import java.util.UUID;

public interface SolicitudContratoUseCase {
    SolicitudContrato crearSolicitud(CrearSolicitudCommand command);

    SolicitudContrato cambiarEstado(CambiarEstadoSolicitudCommand command);

    // Añadimos consultas básicas necesarias para el Front
    List<SolicitudContrato> getSolicitudesPorCliente(UUID clienteId);

    List<SolicitudContrato> getSolicitudesPorProveedor(UUID proveedorId);

    // Borrado cruzado de solicitudes
    void borrarSolicitudesPorUsuarios(UUID usuarioActualId, UUID otroUsuarioId);

    void eliminarSolicitud(UUID id);

    SolicitudContrato pagarSolicitud(UUID solicitudId, UUID clienteId);
}