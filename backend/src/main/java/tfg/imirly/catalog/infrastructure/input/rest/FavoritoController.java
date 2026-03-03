package tfg.imirly.catalog.infrastructure.input.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tfg.imirly.catalog.application.port.in.GetFavoritoUseCase;
import tfg.imirly.catalog.application.port.in.ManageFavoritoUseCase;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/favoritos")
@RequiredArgsConstructor
public class FavoritoController {

    private final ManageFavoritoUseCase manageFavoritoUseCase;
    private final GetFavoritoUseCase getFavoritoUseCase;

    // Método de ayuda para sacar el ID del usuario directamente de su Token (Igual que haces en Anuncios)
    private UUID getUsuarioLogueadoId() {
        String tokenUserId = (String) org.springframework.security.core.context.SecurityContextHolder
                                .getContext().getAuthentication().getCredentials();
        return UUID.fromString(tokenUserId);
    }

    // 1. Añadir a favoritos (El usuario hace click en el corazón vacío)
    @PostMapping("/{anuncioId}")
    public ResponseEntity<?> addFavorito(@PathVariable UUID anuncioId) {
        try {
            manageFavoritoUseCase.addFavorito(getUsuarioLogueadoId(), anuncioId);
            return ResponseEntity.ok(Map.of("message", "Añadido a favoritos"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // 2. Quitar de favoritos (El usuario hace click en el corazón lleno)
    @DeleteMapping("/{anuncioId}")
    public ResponseEntity<?> removeFavorito(@PathVariable UUID anuncioId) {
        manageFavoritoUseCase.removeFavorito(getUsuarioLogueadoId(), anuncioId);
        return ResponseEntity.ok(Map.of("message", "Eliminado de favoritos"));
    }

    // 3. Ver MIS favoritos (Para la pantalla "Favoritos" de la barra inferior)
    @GetMapping
    public ResponseEntity<?> getMisFavoritos() {
        // Devuelve la lista de anuncios exactamente igual que el buscador o el listado de categorías
        return ResponseEntity.ok(getFavoritoUseCase.getFavoritosByUsuario(getUsuarioLogueadoId()));
    }

    // 4. Comprobar si un anuncio concreto es mi favorito (Para saber de qué color pintar el corazón al entrar al anuncio)
    @GetMapping("/{anuncioId}/check")
    public ResponseEntity<?> checkIsFavorito(@PathVariable UUID anuncioId) {
        boolean isFav = getFavoritoUseCase.isFavorito(getUsuarioLogueadoId(), anuncioId);
        return ResponseEntity.ok(Map.of("isFavorito", isFav));
    }
}