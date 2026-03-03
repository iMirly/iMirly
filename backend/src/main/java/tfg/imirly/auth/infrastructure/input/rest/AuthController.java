package tfg.imirly.auth.infrastructure.input.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import tfg.imirly.auth.application.port.in.LoginUserCommand;
import tfg.imirly.auth.application.port.in.LoginUserUseCase;
import tfg.imirly.auth.application.port.in.RegisterUserCommand;
import tfg.imirly.auth.application.port.in.RegisterUserUseCase;
import tfg.imirly.auth.domain.model.User;
import tfg.imirly.auth.domain.model.UserType;
import tfg.imirly.auth.infrastructure.input.rest.dto.LoginUserRequest;
import tfg.imirly.auth.infrastructure.input.rest.dto.RegisterUserRequest;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    // Inyectamos el Puerto de Entrada (El Caso de Uso)
    private final RegisterUserUseCase registerUserUseCase;

    private final LoginUserUseCase loginUserUseCase;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody RegisterUserRequest request) {
        try {
            // 1. Mapear el DTO al Comando de Aplicación
            RegisterUserCommand command = new RegisterUserCommand(
                    request.getNombre(),
                    request.getEmail(),
                    request.getRawPassword(),
                    UserType.valueOf(request.getTipoUsuario().toUpperCase()), // Convierte String a Enum
                    request.getDocumentoIdentidad()
            );

            // 2. Ejecutar el caso de uso
            User newUser = registerUserUseCase.registerUser(command);

            // 3. Devolver respuesta de éxito (201 Created)
            return ResponseEntity.status(HttpStatus.CREATED).body("Usuario registrado con ID: " + newUser.getId());
            
        } catch (IllegalArgumentException e) {
            // Si el email ya existe o el tipo de usuario es inválido (400 Bad Request)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody LoginUserRequest request) {
        try {
            // 1. Mapear DTO a Comando
            LoginUserCommand command = new LoginUserCommand(
                    request.getEmail(),
                    request.getRawPassword()
            );

            // 2. Ejecutar Caso de Uso
            String token = loginUserUseCase.login(command);

            // 3. Devolver Token JWT
            return ResponseEntity.ok(token); // 200 OK
            
        } catch (IllegalArgumentException e) {
            // Si las credenciales fallan, devolvemos 401 Unauthorized
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }
}