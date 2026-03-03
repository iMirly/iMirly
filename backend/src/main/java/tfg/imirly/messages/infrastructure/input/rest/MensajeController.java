package tfg.imirly.messages.infrastructure.input.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import tfg.imirly.auth.infrastructure.output.persistence.entity.UserEntity;
import tfg.imirly.messages.application.port.in.EnviarMensajeCommand;
import tfg.imirly.messages.application.port.in.MensajeUseCase;
import tfg.imirly.messages.domain.model.Mensaje;
import tfg.imirly.messages.infrastructure.input.rest.dto.EnviarMensajeRequest;
import tfg.imirly.messages.infrastructure.input.rest.dto.MensajeResponse;
import tfg.imirly.auth.infrastructure.output.persistence.repository.SpringDataUserRepository;

import java.util.UUID;
import java.util.stream.Collectors;
import java.util.List;

@RestController
@RequestMapping("/api/v1/mensajes")
@RequiredArgsConstructor
public class MensajeController {

    private final MensajeUseCase useCase;
    private final SpringDataUserRepository userRepository;

    // --- 1. ENVIAR UN MENSAJE ---
    @PostMapping
    public ResponseEntity<?> enviarMensaje(@RequestBody EnviarMensajeRequest request) {
        try {
            // 1. El token nos da el EMAIL (Subject)
            String emailRemitente = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

            // 2. BUSCAMOS EL ID REAL (UUID) en la BD usando el email
            // Debes inyectar el repositorio de usuarios en este controlador
            UserEntity usuario = userRepository.findByEmail(emailRemitente)
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

            UUID remitenteId = usuario.getId();

            EnviarMensajeCommand command = new EnviarMensajeCommand(
                    remitenteId,
                    request.getReceptorId(),
                    request.getSolicitudId(),
                    request.getContenido(),
                    request.getTipo() != null ? request.getTipo() : tfg.imirly.messages.domain.model.TipoMensaje.TEXTO);

            Mensaje guardado = useCase.enviarMensaje(command);
            return ResponseEntity.status(HttpStatus.CREATED).body(toResponse(guardado));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // --- 2. OBTENER LA CONVERSACIÓN ENTRE DOS USUARIOS ---
    @GetMapping("/conversacion/{otroUsuarioId}")
    public ResponseEntity<?> obtenerConversacion(@PathVariable UUID otroUsuarioId) {
        try {
            String emailActual = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            UserEntity usuario = userRepository.findByEmail(emailActual)
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

            // 🚨 IMPORTANTE: Mapear a MensajeResponse antes de enviar 🚨
            List<MensajeResponse> conversacion = useCase.obtenerConversacion(usuario.getId(), otroUsuarioId)
                    .stream()
                    .map(this::toResponse)
                    .collect(Collectors.toList());

            return ResponseEntity.ok(conversacion);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // --- 3. BORRAR LA CONVERSACIÓN ---
    @DeleteMapping("/conversacion/{otroUsuarioId}")
    public ResponseEntity<?> borrarConversacion(@PathVariable UUID otroUsuarioId) {
        try {
            String emailActual = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            UserEntity usuario = userRepository.findByEmail(emailActual)
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

            useCase.borrarConversacion(usuario.getId(), otroUsuarioId);

            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Método auxiliar para transformar el modelo de dominio al DTO de salida
    private MensajeResponse toResponse(Mensaje m) {
        return new MensajeResponse(
                m.getId(),
                m.getRemitenteId(),
                m.getReceptorId(),
                m.getSolicitudId(), // <--- NUEVO
                m.getContenido(),
                m.getTipo(),        // <--- NUEVO
                m.getTimestamp(),
                m.isLeido()
        );
    }
}