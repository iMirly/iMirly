package tfg.imirly.catalog.application.port.in;

import tfg.imirly.catalog.domain.model.AnuncioServicio;
import java.util.List;
import java.util.UUID;

public interface GetFavoritoUseCase {
    // Devuelve los anuncios reales para poder pintarlos en la pantalla
    List<AnuncioServicio> getFavoritosByUsuario(UUID usuarioId);
    
    // Para que el corazón salga pintado o vacío al entrar a un anuncio
    boolean isFavorito(UUID usuarioId, UUID anuncioId);
}