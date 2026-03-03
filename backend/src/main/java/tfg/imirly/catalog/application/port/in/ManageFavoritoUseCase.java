package tfg.imirly.catalog.application.port.in;

import java.util.UUID;

public interface ManageFavoritoUseCase {
    void addFavorito(UUID usuarioId, UUID anuncioId);
    void removeFavorito(UUID usuarioId, UUID anuncioId);
}