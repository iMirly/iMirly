package tfg.imirly.catalog.infrastructure.output.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tfg.imirly.catalog.infrastructure.output.persistence.entity.FavoritoEntity;

import java.util.List;
import java.util.UUID;

public interface FavoritoRepository extends JpaRepository<FavoritoEntity, UUID> {
    boolean existsByUsuarioIdAndAnuncioId(UUID usuarioId, UUID anuncioId);
    void deleteByUsuarioIdAndAnuncioId(UUID usuarioId, UUID anuncioId);
    List<FavoritoEntity> findByUsuarioId(UUID usuarioId);
}