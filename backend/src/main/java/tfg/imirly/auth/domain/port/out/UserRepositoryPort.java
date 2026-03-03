package tfg.imirly.auth.domain.port.out;

import java.util.Optional;
import java.util.UUID;

import tfg.imirly.auth.domain.model.User;

public interface UserRepositoryPort {
    User save(User user);
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
    Optional<User> findById(UUID id);
}
