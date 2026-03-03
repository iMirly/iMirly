package tfg.imirly.catalog.infrastructure.output.persistence;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import tfg.imirly.catalog.domain.model.Favorito;
import tfg.imirly.catalog.domain.port.out.FavoritoRepositoryPort;
import tfg.imirly.catalog.infrastructure.output.persistence.entity.FavoritoEntity;
import tfg.imirly.catalog.infrastructure.output.persistence.repository.FavoritoRepository;

import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class FavoritoRepositoryAdapter implements FavoritoRepositoryPort {

    private final FavoritoRepository springDataRepository;

    @Override
    public Favorito save(Favorito favorito) {
        FavoritoEntity entity = new FavoritoEntity(
                favorito.getId(),
                favorito.getUsuarioId(),
                favorito.getAnuncioId(),
                favorito.getFechaGuardado()
        );
        FavoritoEntity savedEntity = springDataRepository.save(entity);
        return new Favorito(savedEntity.getId(), savedEntity.getUsuarioId(), savedEntity.getAnuncioId(), savedEntity.getFechaGuardado());
    }

    @Override
    @Transactional // Necesario para borrar en JPA
    public void deleteByUsuarioIdAndAnuncioId(UUID usuarioId, UUID anuncioId) {
        springDataRepository.deleteByUsuarioIdAndAnuncioId(usuarioId, anuncioId);
    }

    @Override
    public boolean existsByUsuarioIdAndAnuncioId(UUID usuarioId, UUID anuncioId) {
        return springDataRepository.existsByUsuarioIdAndAnuncioId(usuarioId, anuncioId);
    }

    @Override
    public List<Favorito> findByUsuarioId(UUID usuarioId) {
        return springDataRepository.findByUsuarioId(usuarioId).stream()
                .map(entity -> new Favorito(entity.getId(), entity.getUsuarioId(), entity.getAnuncioId(), entity.getFechaGuardado()))
                .collect(Collectors.toList());
    }
}