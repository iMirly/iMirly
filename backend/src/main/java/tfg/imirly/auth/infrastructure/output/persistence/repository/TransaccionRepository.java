package tfg.imirly.auth.infrastructure.output.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tfg.imirly.auth.infrastructure.output.persistence.entity.TransaccionEntity;

import java.util.List;
import java.util.UUID;

@Repository
public interface TransaccionRepository extends JpaRepository<TransaccionEntity, UUID> {
    List<TransaccionEntity> findByUsuarioIdOrderByFechaDesc(UUID usuarioId);
}
