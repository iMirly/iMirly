package tfg.imirly.contracting.infrastructure.input.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tfg.imirly.contracting.application.port.in.CambiarEstadoSolicitudCommand;
import tfg.imirly.contracting.application.port.in.CrearSolicitudCommand;
import tfg.imirly.contracting.application.port.in.SolicitudContratoUseCase;
import tfg.imirly.contracting.domain.model.SolicitudContrato;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/solicitudes")
@RequiredArgsConstructor
public class SolicitudContratoController {

    private final SolicitudContratoUseCase useCase;

    // --- 1. CREAR UNA NUEVA SOLICITUD (El cliente contacta al profesional) ---
    @PostMapping
    public ResponseEntity<?> crearSolicitud(
            @RequestBody tfg.imirly.contracting.infrastructure.input.rest.dto.CrearSolicitudRequest request) {
        try {
            // Sacamos el ID del cliente directamente del Token de seguridad
            String tokenUserId = (String) org.springframework.security.core.context.SecurityContextHolder
                    .getContext().getAuthentication().getCredentials();
            UUID clienteId = UUID.fromString(tokenUserId);

            // Montamos el comando y lo enviamos al caso de uso
            CrearSolicitudCommand command = new CrearSolicitudCommand(clienteId, request.getAnuncioId(),
                    request.getDetalles());
            return ResponseEntity.status(HttpStatus.CREATED).body(useCase.crearSolicitud(command));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error interno: " + e.getMessage());
        }
    }

    @PatchMapping("/{id}/estado")
    public ResponseEntity<?> cambiarEstado(@PathVariable UUID id,
            @RequestBody tfg.imirly.contracting.infrastructure.input.rest.dto.CambiarEstadoRequest request) {
        try {
            // Sacamos el ID del usuario que está haciendo la petición
            String tokenUserId = (String) org.springframework.security.core.context.SecurityContextHolder
                    .getContext().getAuthentication().getCredentials();
            UUID usuarioId = UUID.fromString(tokenUserId);

            // Le pasamos el nuevo estado desde el JSON del body
            CambiarEstadoSolicitudCommand command = new CambiarEstadoSolicitudCommand(id, request.getNuevoEstado(),
                    usuarioId);
            return ResponseEntity.ok(useCase.cambiarEstado(command));
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error interno: " + e.getMessage());
        }
    }

    // --- 3. VER MIS SOLICITUDES COMO CLIENTE ---
    @GetMapping("/cliente")
    public ResponseEntity<?> getMisSolicitudesComoCliente() {
        try {
            String tokenUserId = (String) org.springframework.security.core.context.SecurityContextHolder
                    .getContext().getAuthentication().getCredentials();
            return ResponseEntity.ok(useCase.getSolicitudesPorCliente(UUID.fromString(tokenUserId)));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // --- 4. VER LAS SOLICITUDES QUE HE RECIBIDO COMO PROFESIONAL ---
    @GetMapping("/proveedor")
    public ResponseEntity<?> getSolicitudesRecibidasComoProveedor() {
        try {
            String tokenUserId = (String) org.springframework.security.core.context.SecurityContextHolder
                    .getContext().getAuthentication().getCredentials();
            return ResponseEntity.ok(useCase.getSolicitudesPorProveedor(UUID.fromString(tokenUserId)));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/conversacion/{otroUsuarioId}")
    public ResponseEntity<?> borrarSolicitudesConversacion(@PathVariable UUID otroUsuarioId) {
        try {
            String tokenUserId = (String) org.springframework.security.core.context.SecurityContextHolder
                    .getContext().getAuthentication().getCredentials();
            UUID usuarioId = UUID.fromString(tokenUserId);
            useCase.borrarSolicitudesPorUsuarios(usuarioId, otroUsuarioId);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // --- 5. ELIMINAR SOLICITUD (Ocultar chat) ---
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarSolicitud(@PathVariable UUID id) {
        try {
            // Llama al caso de uso para borrar la solicitud
            useCase.eliminarSolicitud(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // --- 6. PAGAR PRESUPUESTO ---
    @PostMapping("/{id}/pagar")
    public ResponseEntity<?> pagarSolicitud(@PathVariable UUID id) {
        try {
            // Sacamos quién es el que pulsa el botón desde el token
            String tokenUserId = (String) org.springframework.security.core.context.SecurityContextHolder
                    .getContext().getAuthentication().getCredentials();
            UUID clienteId = UUID.fromString(tokenUserId);

            // Ejecutamos el pago
            SolicitudContrato pagada = useCase.pagarSolicitud(id, clienteId);
            return ResponseEntity.ok(pagada);
            
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error interno: " + e.getMessage());
        }
    }
}