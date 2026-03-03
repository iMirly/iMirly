package tfg.imirly.contracting.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import tfg.imirly.contracting.application.port.in.CambiarEstadoSolicitudCommand;
import tfg.imirly.contracting.application.port.in.CrearSolicitudCommand;
import tfg.imirly.contracting.application.port.in.SolicitudContratoUseCase;
import tfg.imirly.contracting.domain.model.SolicitudContrato;
import tfg.imirly.contracting.domain.model.EstadoSolicitud;
import tfg.imirly.contracting.domain.port.out.SolicitudContratoRepositoryPort;
import tfg.imirly.catalog.domain.port.out.AnuncioServicioRepositoryPort; // Necesitaremos verificar el anuncio

import java.util.UUID;
import java.util.List;

import tfg.imirly.auth.domain.model.User;
import tfg.imirly.auth.domain.port.out.UserRepositoryPort;

@Service
@RequiredArgsConstructor
public class SolicitudContratoService implements SolicitudContratoUseCase {

    private final SolicitudContratoRepositoryPort repositoryPort;
    private final AnuncioServicioRepositoryPort anuncioRepositoryPort;
    private final UserRepositoryPort userRepository; // Para validar el pago
    private final tfg.imirly.auth.infrastructure.output.persistence.repository.TransaccionRepository transaccionRepository;

    @Override
    public SolicitudContrato crearSolicitud(CrearSolicitudCommand command) {
        anuncioRepositoryPort.findById(command.getAnuncioId())
                .orElseThrow(() -> new IllegalArgumentException("El anuncio no existe."));

        SolicitudContrato nueva = new SolicitudContrato(
                command.getClienteId(),
                command.getAnuncioId(),
                command.getDetalles());
        return repositoryPort.save(nueva);
    }

    @Override
    public SolicitudContrato cambiarEstado(CambiarEstadoSolicitudCommand command) {
        SolicitudContrato solicitud = repositoryPort.findById(command.getSolicitudId())
                .orElseThrow(() -> new IllegalArgumentException("Solicitud no encontrada."));

        if (command.getNuevoEstado() == EstadoSolicitud.RECHAZADO) {
            repositoryPort.deleteById(solicitud.getId());
            solicitud.setEstado(EstadoSolicitud.RECHAZADO);
            return solicitud;
        }

        // 👇 ARREGLO PARA EL BOTÓN DE RECHAZAR 👇
        if (command.getNuevoEstado() == EstadoSolicitud.ACEPTADO) {
            if (solicitud.getEstado() == EstadoSolicitud.PENDIENTE) {
                solicitud.aceptar(); // 1. Profesional acepta la solicitud inicial
            } else if (solicitud.getEstado() == EstadoSolicitud.PENDIENTE_PAGO) {
                solicitud.setEstado(EstadoSolicitud.ACEPTADO); // 2. Cliente rechaza el presupuesto (vuelve atrás)
            } else {
                throw new IllegalStateException("No se puede cambiar a ACEPTADO desde el estado actual.");
            }
        } else {
            solicitud.setEstado(command.getNuevoEstado());

            // 👇 NUEVO: Si el trabajo está COMPLETADO, se ingresa el saldo al proveedor 👇
            if (command.getNuevoEstado() == EstadoSolicitud.COMPLETADO) {
                User proveedor = userRepository.findById(solicitud.getProveedorId())
                        .orElseThrow(() -> new IllegalArgumentException("Proveedor no encontrado."));
                proveedor.sumarSaldo(solicitud.getPrecioAcordado());
                userRepository.save(proveedor);

                tfg.imirly.auth.infrastructure.output.persistence.entity.TransaccionEntity tr = new tfg.imirly.auth.infrastructure.output.persistence.entity.TransaccionEntity(
                        null, proveedor.getId(), solicitud.getPrecioAcordado(), "SERVICIO_COBRADO",
                        "Cobro por trabajo en iMirly", java.time.LocalDateTime.now());
                transaccionRepository.save(tr);
            }
        }

        return repositoryPort.save(solicitud);
    }

    @Override
    public List<SolicitudContrato> getSolicitudesPorCliente(UUID clienteId) {
        return repositoryPort.findByClienteId(clienteId);
    }

    @Override
    public List<SolicitudContrato> getSolicitudesPorProveedor(UUID proveedorId) {
        return repositoryPort.findByProveedorId(proveedorId);
    }

    @Override
    public void borrarSolicitudesPorUsuarios(UUID usuarioActualId, UUID otroUsuarioId) {
        repositoryPort.borrarSolicitudesEntreUsuarios(usuarioActualId, otroUsuarioId);
    }

    @Override
    public void eliminarSolicitud(UUID id) {
        SolicitudContrato solicitud = repositoryPort.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Solicitud no encontrada"));

        // Bloqueamos el borrado si hay un presupuesto pendiente de pago o ya pagado
        if (solicitud.getEstado() == EstadoSolicitud.PENDIENTE_PAGO ||
                solicitud.getEstado() == EstadoSolicitud.PAGADO ||
                solicitud.getEstado() == EstadoSolicitud.EN_CURSO ||
                solicitud.getEstado() == EstadoSolicitud.REVISION_PENDIENTE) {
            throw new IllegalStateException(
                    "No se puede borrar el chat si hay un presupuesto aceptado (solo antes o al finalizar el trabajo).");
        }

        repositoryPort.deleteById(id);
    }

    @Override
    @Transactional // O todo sale bien, o se deshace todo (vital para el dinero)
    public SolicitudContrato pagarSolicitud(UUID solicitudId, UUID clienteId) {
        // 1. Buscamos el contrato
        SolicitudContrato solicitud = repositoryPort.findById(solicitudId)
                .orElseThrow(() -> new IllegalArgumentException("Solicitud no encontrada."));

        // 2. Seguridad: Validamos que el que paga es el dueño de la solicitud
        if (!solicitud.getClienteId().equals(clienteId)) {
            throw new IllegalStateException("No tienes permiso para pagar esta solicitud.");
        }

        // 3. Buscamos al usuario cliente (Ajusta 'userRepository' al nombre de tu
        // repositorio real)
        User cliente = userRepository.findById(clienteId)
                .orElseThrow(() -> new IllegalArgumentException("Cliente no encontrado."));

        // 4. Restamos el saldo y creamos transaccion
        cliente.restarSaldo(solicitud.getPrecioAcordado());
        userRepository.save(cliente);

        tfg.imirly.auth.infrastructure.output.persistence.entity.TransaccionEntity tr = new tfg.imirly.auth.infrastructure.output.persistence.entity.TransaccionEntity(
                null, cliente.getId(), solicitud.getPrecioAcordado(), "SERVICIO_PAGADO",
                "Pago de presupuesto en iMirly", java.time.LocalDateTime.now());
        transaccionRepository.save(tr);

        // 5. Cambiamos el estado del contrato al "Limbo"
        solicitud.pagarPresupuesto();
        return repositoryPort.save(solicitud);
    }
}