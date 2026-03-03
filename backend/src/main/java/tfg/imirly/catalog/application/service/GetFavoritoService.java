package tfg.imirly.catalog.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tfg.imirly.catalog.application.port.in.GetFavoritoUseCase;
import tfg.imirly.catalog.domain.model.AnuncioServicio;
import tfg.imirly.catalog.domain.model.Favorito;
import tfg.imirly.catalog.domain.port.out.AnuncioServicioRepositoryPort;
import tfg.imirly.catalog.domain.port.out.FavoritoRepositoryPort;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GetFavoritoService implements GetFavoritoUseCase {

    private final FavoritoRepositoryPort favoritoRepositoryPort;
    private final AnuncioServicioRepositoryPort anuncioRepositoryPort;

    @Override
    public List<AnuncioServicio> getFavoritosByUsuario(UUID usuarioId) {
        // 1. Buscamos las relaciones (los "likes") de este usuario
        List<Favorito> favoritos = favoritoRepositoryPort.findByUsuarioId(usuarioId);

        // 2. Transformamos esos likes en los Anuncios reales buscando sus IDs
        return favoritos.stream()
                .map(fav -> anuncioRepositoryPort.findById(fav.getAnuncioId()))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }

    @Override
    public boolean isFavorito(UUID usuarioId, UUID anuncioId) {
        return favoritoRepositoryPort.existsByUsuarioIdAndAnuncioId(usuarioId, anuncioId);
    }
}