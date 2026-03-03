package tfg.imirly.messages.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tfg.imirly.messages.application.port.in.EnviarMensajeCommand;
import tfg.imirly.messages.application.port.in.MensajeUseCase;
import tfg.imirly.messages.domain.model.Mensaje;
import tfg.imirly.messages.domain.model.TipoMensaje;
import tfg.imirly.messages.domain.port.out.MensajeRepositoryPort;
import tfg.imirly.contracting.domain.model.SolicitudContrato;
import tfg.imirly.contracting.domain.port.out.SolicitudContratoRepositoryPort;
import tfg.imirly.contracting.application.port.in.SolicitudContratoUseCase;
import tfg.imirly.contracting.application.port.in.CambiarEstadoSolicitudCommand;
import tfg.imirly.contracting.domain.model.EstadoSolicitud;
import tfg.imirly.auth.domain.port.out.UserRepositoryPort;
import tfg.imirly.auth.domain.model.User;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MensajeService implements MensajeUseCase {

    private final MensajeRepositoryPort repositoryPort;
    private final SolicitudContratoRepositoryPort solicitudRepositoryPort;
    private final SolicitudContratoUseCase solicitudContratoUseCase;
    private final UserRepositoryPort userRepositoryPort;

    @Override
    @Transactional // O se guarda el mensaje y se cambia el precio, o no se hace nada
    public Mensaje enviarMensaje(EnviarMensajeCommand command) {
        // 1. Validaciones básicas
        if (command.getContenido() == null || command.getContenido().trim().isEmpty()) {
            throw new IllegalArgumentException("El mensaje no puede estar vacío.");
        }

        // 2. LÓGICA DE NEGOCIO FASE 2: Si es un presupuesto, actualizamos la solicitud
        if (command.getTipo() == TipoMensaje.PROPUESTA_PRECIO) {
            actualizarSolicitudConPrecio(command);
        } else if (command.getTipo() == TipoMensaje.TRABAJO_FINALIZADO) {
            SolicitudContrato solicitud = solicitudRepositoryPort.findById(command.getSolicitudId())
                    .orElseThrow(() -> new IllegalArgumentException("Solicitud no encontrada"));
            solicitud.marcarComoTerminado();
            solicitudRepositoryPort.save(solicitud);
        } else if (command.getTipo() == TipoMensaje.SERVICIO_COMPLETADO) {
            solicitudContratoUseCase.cambiarEstado(new CambiarEstadoSolicitudCommand(
                    command.getSolicitudId(), EstadoSolicitud.COMPLETADO, command.getRemitenteId()));
        } else if (command.getTipo() == TipoMensaje.SERVICIO_RECHAZADO) {
            SolicitudContrato solicitud = solicitudRepositoryPort.findById(command.getSolicitudId())
                    .orElseThrow(() -> new IllegalArgumentException("Solicitud no encontrada"));
            solicitud.validarTrabajo(false); // Vuelve a PAGADO
            solicitudRepositoryPort.save(solicitud);
        } else if (command.getTipo() == TipoMensaje.VALORACION) {
            User valorado = userRepositoryPort.findById(command.getReceptorId())
                    .orElseThrow(() -> new IllegalArgumentException("Usuario a valorar no encontrado"));

            try {
                String[] partes = command.getContenido().split("\\|");
                Double nota = Double.parseDouble(partes[0].trim());
                valorado.addValoracion(nota);
                userRepositoryPort.save(valorado);
            } catch (Exception e) {
                throw new IllegalArgumentException("El formato de la valoración es incorrecto.");
            }
        }

        // 3. Crear el objeto de Dominio
        Mensaje nuevoMensaje = new Mensaje(
                command.getRemitenteId(),
                command.getReceptorId(),
                command.getSolicitudId(),
                command.getContenido(),
                command.getTipo());

        // 4. Guardar y devolver
        return repositoryPort.save(nuevoMensaje);
    }

    private void actualizarSolicitudConPrecio(EnviarMensajeCommand command) {
        SolicitudContrato solicitud = solicitudRepositoryPort.findById(command.getSolicitudId())
                .orElseThrow(() -> new IllegalArgumentException("Solicitud no encontrada"));

        try {
            // Separamos el contenido que manda Android: "50.0|Descripción del trabajo"
            String[] partes = command.getContenido().split("\\|");
            Double precio = Double.parseDouble(partes[0].trim()); // El precio siempre será la primera parte

            solicitud.proponerPrecio(precio);
            solicitudRepositoryPort.save(solicitud);
        } catch (Exception e) {
            throw new IllegalArgumentException("El formato del presupuesto es incorrecto.");
        }
    }

    @Override
    public List<Mensaje> obtenerConversacion(UUID usuarioActualId, UUID otroUsuarioId) {
        repositoryPort.marcarMensajesComoLeidos(otroUsuarioId, usuarioActualId);
        return repositoryPort.findConversacion(usuarioActualId, otroUsuarioId);
    }

    @Override
    public void borrarConversacion(UUID usuarioActualId, UUID otroUsuarioId) {
        repositoryPort.borrarConversacion(usuarioActualId, otroUsuarioId);
    }
}