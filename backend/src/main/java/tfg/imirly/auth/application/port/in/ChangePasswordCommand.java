package tfg.imirly.auth.application.port.in;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ChangePasswordCommand {
    private String email;
    private String oldPassword;
    private String newPassword;
}