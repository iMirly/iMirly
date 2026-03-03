package tfg.imirly.auth.infrastructure.output.persistence;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import tfg.imirly.auth.domain.model.User;
import tfg.imirly.auth.domain.port.out.UserRepositoryPort;
import tfg.imirly.auth.infrastructure.output.persistence.entity.UserEntity;
import tfg.imirly.auth.infrastructure.output.persistence.repository.SpringDataUserRepository;

import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class UserRepositoryAdapter implements UserRepositoryPort {
    
    private final SpringDataUserRepository springDataRepository;
    private final UserMapper userMapper;

    @Override
    public User save(User user) {
        // 1. Traducimos el usuario puro a entidad de base de datos
        UserEntity entity = userMapper.toEntity(user);
        // 2. Guardamos en PostgreSQL usando la magia de Spring
        UserEntity savedEntity = springDataRepository.save(entity);
        // 3. Devolvemos el usuario puro traducido de vuelta
        return userMapper.toDomain(savedEntity);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return springDataRepository.findByEmail(email)
                .map(userMapper::toDomain); // Traduce si lo encuentra
    }

    @Override
    public boolean existsByEmail(String email) {
        return springDataRepository.existsByEmail(email);
    }

    @Override
    public Optional<User> findById(UUID id) {
        return springDataRepository.findById(id)
                .map(userMapper::toDomain);
    }
}