package tfg.imirly.auth.infrastructure.output.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import tfg.imirly.auth.infrastructure.output.persistence.entity.UserEntity;

import java.util.Optional;
import java.util.UUID;

public interface SpringDataUserRepository extends JpaRepository<UserEntity, UUID> {
    Optional<UserEntity> findByEmail(String email);
    boolean existsByEmail(String email);
}