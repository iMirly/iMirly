package tfg.imirly.auth.application.port.in;

public interface ChangePasswordUseCase {
    void changePassword(ChangePasswordCommand command);
}