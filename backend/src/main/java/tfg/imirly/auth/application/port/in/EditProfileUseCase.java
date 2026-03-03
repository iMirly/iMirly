package tfg.imirly.auth.application.port.in;

import tfg.imirly.auth.domain.model.User;

public interface EditProfileUseCase {
    User editProfile(EditProfileCommand command);
}