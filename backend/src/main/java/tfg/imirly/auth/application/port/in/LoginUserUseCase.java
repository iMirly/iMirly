package tfg.imirly.auth.application.port.in;

public interface LoginUserUseCase {
    String login(LoginUserCommand command); // Devuelve el token JWT (String)
}