package tfg.imirly.auth.application.port.in;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LoginUserCommand {
    private String email;
    private String rawPassword;
}