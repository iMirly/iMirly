package tfg.imirly.catalog.domain.port.out;

import tfg.imirly.catalog.domain.model.Favorito;
import java.util.List;
import java.util.UUID;

public interface FavoritoRepositoryPort {
    Favorito save(Favorito favorito);
    void deleteByUsuarioIdAndAnuncioId(UUID usuarioId, UUID anuncioId);
    boolean existsByUsuarioIdAndAnuncioId(UUID usuarioId, UUID anuncioId);
    List<Favorito> findByUsuarioId(UUID usuarioId);
}