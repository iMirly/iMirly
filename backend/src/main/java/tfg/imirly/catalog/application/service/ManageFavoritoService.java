package tfg.imirly.catalog.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tfg.imirly.catalog.application.port.in.ManageFavoritoUseCase;
import tfg.imirly.catalog.domain.model.Favorito;
import tfg.imirly.catalog.domain.port.out.AnuncioServicioRepositoryPort;
import tfg.imirly.catalog.domain.port.out.FavoritoRepositoryPort;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ManageFavoritoService implements ManageFavoritoUseCase {

    private final FavoritoRepositoryPort favoritoRepositoryPort;
    private final AnuncioServicioRepositoryPort anuncioRepositoryPort;

    @Override
    public void addFavorito(UUID usuarioId, UUID anuncioId) {
        // 1. Verificamos que el anuncio existe antes de guardarlo
        if (anuncioRepositoryPort.findById(anuncioId).isEmpty()) {
            throw new IllegalArgumentException("El anuncio no existe");
        }
        
        // 2. Comprobamos que no le haya dado like ya
        if (!favoritoRepositoryPort.existsByUsuarioIdAndAnuncioId(usuarioId, anuncioId)) {
            Favorito favorito = new Favorito(usuarioId, anuncioId);
            favoritoRepositoryPort.save(favorito);
        }
    }

    @Override
    public void removeFavorito(UUID usuarioId, UUID anuncioId) {
        favoritoRepositoryPort.deleteByUsuarioIdAndAnuncioId(usuarioId, anuncioId);
    }
}