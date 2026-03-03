package tfg.imirly.auth.infrastructure.input.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import tfg.imirly.auth.application.port.in.ChangePasswordCommand;
import tfg.imirly.auth.application.port.in.ChangePasswordUseCase;
import tfg.imirly.auth.application.port.in.EditProfileCommand;
import tfg.imirly.auth.application.port.in.EditProfileUseCase;
import tfg.imirly.auth.application.port.in.GetUserUseCase;
import tfg.imirly.auth.domain.model.User;
import tfg.imirly.auth.infrastructure.input.rest.dto.EditProfileRequest;
import tfg.imirly.auth.infrastructure.input.rest.dto.UserProfileResponse;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

        private final EditProfileUseCase editProfileUseCase;
        private final GetUserUseCase getUserUseCase;
        private final ChangePasswordUseCase changePasswordUseCase;
        private final tfg.imirly.auth.domain.port.out.UserRepositoryPort userRepository;
        private final tfg.imirly.auth.infrastructure.output.persistence.repository.TransaccionRepository transaccionRepository;
        private final tfg.imirly.contracting.application.port.in.SolicitudContratoUseCase solicitudContratoUseCase;

        @GetMapping("/profile/{email}")
        public ResponseEntity<?> getUserProfile(@PathVariable String email) {
                try {
                        // CORRECCIÓN: Usamos getName() para obtener el "sub" (el email)
                        String tokenEmail = org.springframework.security.core.context.SecurityContextHolder
                                        .getContext().getAuthentication().getName();

                        // Si el email de la URL no coincide con el del Token, bloqueamos
                        if (!email.equals(tokenEmail)) {
                                // Ahora el error te dirá exactamente qué está comparando
                                return ResponseEntity.status(403)
                                                .body("Prohibido. URL intentada: " + email + " | Email en Token: "
                                                                + tokenEmail);
                        }

                        // Buscamos al usuario y montamos la respuesta
                        User user = getUserUseCase.getUserByEmail(email);
                        UserProfileResponse response = new UserProfileResponse(
                                        user.getId(), user.getNombre(), user.getEmail(), user.getDocumentoIdentidad(),
                                        user.getSaldo(), user.getValoracionMedia(), user.getNumeroValoraciones());
                        return ResponseEntity.ok(response);

                } catch (IllegalArgumentException e) {
                        return ResponseEntity.badRequest().body(e.getMessage());
                }
        }

        // Usamos PUT porque vamos a modificar un recurso existente
        @PutMapping("/profile/{email}")
        public ResponseEntity<?> editProfile(@PathVariable String email, @RequestBody EditProfileRequest request) {
                try {
                        // Leemos el email del token
                        String tokenEmail = org.springframework.security.core.context.SecurityContextHolder
                                        .getContext().getAuthentication().getName();

                        if (!email.equals(tokenEmail)) {
                                return ResponseEntity.status(403)
                                                .body("Prohibido: No puedes editar el perfil de otro usuario.");
                        }

                        // 1. Buscamos el ID del usuario usando su email
                        User user = getUserUseCase.getUserByEmail(email);

                        // 2. Ejecutamos el comando con su ID real
                        EditProfileCommand command = new EditProfileCommand(user.getId(), request.getNombre(),
                                        request.getDocumentoIdentidad(), request.getEmail());
                        User updatedUser = editProfileUseCase.editProfile(command);

                        return ResponseEntity.ok(
                                        "Perfil actualizado correctamente. Nuevo nombre: " + updatedUser.getNombre());

                } catch (IllegalArgumentException e) {
                        return ResponseEntity.badRequest().body(e.getMessage());
                }
        }

        @PutMapping("/password")
        public ResponseEntity<?> changePassword(
                        @RequestBody tfg.imirly.auth.infrastructure.input.rest.dto.ChangePasswordRequest request) {
                try {
                        // Sacamos el email de la persona que está intentando cambiar la contraseña
                        // directamente de su Token (súper seguro)
                        String email = org.springframework.security.core.context.SecurityContextHolder
                                        .getContext().getAuthentication().getName();

                        ChangePasswordCommand command = new ChangePasswordCommand(email, request.getOldPassword(),
                                        request.getNewPassword());
                        changePasswordUseCase.changePassword(command);

                        return ResponseEntity.ok().body("{\"message\": \"Contraseña actualizada correctamente\"}");
                } catch (IllegalArgumentException e) {
                        return ResponseEntity.badRequest().body("{\"error\": \"" + e.getMessage() + "\"}");
                }
        }

        @PostMapping("/saldo/ingresar")
        public ResponseEntity<?> ingresarSaldo() {
                try {
                        String tokenEmail = org.springframework.security.core.context.SecurityContextHolder
                                        .getContext().getAuthentication().getName();
                        User user = getUserUseCase.getUserByEmail(tokenEmail);
                        user.sumarSaldo(10.0); // TODO: Implementar pago con banco real
                        userRepository.save(user);

                        tfg.imirly.auth.infrastructure.output.persistence.entity.TransaccionEntity tr = new tfg.imirly.auth.infrastructure.output.persistence.entity.TransaccionEntity(
                                        null, user.getId(), 10.0, "INGRESO", "Ingreso de saldo",
                                        java.time.LocalDateTime.now());
                        transaccionRepository.save(tr);

                        return ResponseEntity.ok().body("{\"message\": \"Saldo ingresado correctamente\"}");
                } catch (IllegalArgumentException e) {
                        return ResponseEntity.badRequest().body("{\"error\": \"" + e.getMessage() + "\"}");
                }
        }

        @PostMapping("/saldo/retirar")
        public ResponseEntity<?> retirarSaldo() {
                try {
                        String tokenEmail = org.springframework.security.core.context.SecurityContextHolder
                                        .getContext().getAuthentication().getName();
                        User user = getUserUseCase.getUserByEmail(tokenEmail);
                        user.restarSaldo(10.0); // TODO: Implementar transferencia a banco real
                        userRepository.save(user);

                        tfg.imirly.auth.infrastructure.output.persistence.entity.TransaccionEntity tr = new tfg.imirly.auth.infrastructure.output.persistence.entity.TransaccionEntity(
                                        null, user.getId(), 10.0, "RETIRO", "Retirada a cuenta bancaria",
                                        java.time.LocalDateTime.now());
                        transaccionRepository.save(tr);

                        return ResponseEntity.ok().body("{\"message\": \"Saldo retirado correctamente\"}");
                } catch (IllegalArgumentException e) {
                        return ResponseEntity.badRequest().body("{\"error\": \"" + e.getMessage() + "\"}");
                }
        }

        @GetMapping("/wallet")
        public ResponseEntity<?> getWallet() {
                try {
                        String tokenEmail = org.springframework.security.core.context.SecurityContextHolder
                                        .getContext().getAuthentication().getName();
                        User user = getUserUseCase.getUserByEmail(tokenEmail);

                        // Calcular pendiente: Solicitudes como PROVEEDOR en estado PAGADO, EN_CURSO,
                        // REVISION_PENDIENTE
                        java.util.List<tfg.imirly.contracting.domain.model.SolicitudContrato> solicitudes = solicitudContratoUseCase
                                        .getSolicitudesPorProveedor(user.getId());
                        Double pendiente = solicitudes.stream()
                                        .filter(s -> s.getEstado() == tfg.imirly.contracting.domain.model.EstadoSolicitud.PAGADO
                                                        ||
                                                        s.getEstado() == tfg.imirly.contracting.domain.model.EstadoSolicitud.EN_CURSO
                                                        ||
                                                        s.getEstado() == tfg.imirly.contracting.domain.model.EstadoSolicitud.REVISION_PENDIENTE)
                                        .mapToDouble(tfg.imirly.contracting.domain.model.SolicitudContrato::getPrecioAcordado)
                                        .sum();

                        // Transacciones
                        java.util.List<tfg.imirly.auth.infrastructure.output.persistence.entity.TransaccionEntity> transacciones = transaccionRepository
                                        .findByUsuarioIdOrderByFechaDesc(user.getId());

                        Double esteMes = transacciones.stream()
                                        .filter(t -> t.getFecha().getMonth() == java.time.LocalDateTime.now().getMonth()
                                                        &&
                                                        t.getFecha().getYear() == java.time.LocalDateTime.now()
                                                                        .getYear())
                                        .filter(t -> t.getTipo().equals("INGRESO")
                                                        || t.getTipo().equals("SERVICIO_COBRADO"))
                                        .mapToDouble(
                                                        tfg.imirly.auth.infrastructure.output.persistence.entity.TransaccionEntity::getCantidad)
                                        .sum();

                        java.util.List<tfg.imirly.auth.infrastructure.input.rest.dto.TransaccionDTO> recientes = transacciones
                                        .stream()
                                        .limit(10)
                                        .map(t -> {
                                                String amt = t.getTipo().equals("RETIRO")
                                                                || t.getTipo().equals("SERVICIO_PAGADO")
                                                                                ? "-" + String.format("%.2f€",
                                                                                                t.getCantidad())
                                                                                : "+" + String.format("%.2f€",
                                                                                                t.getCantidad());
                                                return new tfg.imirly.auth.infrastructure.input.rest.dto.TransaccionDTO(
                                                                t.getDescripcion(),
                                                                t.getTipo(),
                                                                amt,
                                                                "Completado",
                                                                t.getFecha().toLocalDate().toString());
                                        }).collect(java.util.stream.Collectors.toList());

                        tfg.imirly.auth.infrastructure.input.rest.dto.SaldoResponse resp = new tfg.imirly.auth.infrastructure.input.rest.dto.SaldoResponse(
                                        user.getSaldo(), pendiente, esteMes, recientes);
                        return ResponseEntity.ok(resp);
                } catch (IllegalArgumentException e) {
                        return ResponseEntity.badRequest().body("{\"error\": \"" + e.getMessage() + "\"}");
                }
        }

}