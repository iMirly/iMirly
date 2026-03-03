package tfg.imirly.catalog.infrastructure.input.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tfg.imirly.catalog.application.port.in.GetCatalogUseCase;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/catalog")
@RequiredArgsConstructor
public class CatalogController {

    private final GetCatalogUseCase getCatalogUseCase;

    @GetMapping("/categories")
    public ResponseEntity<?> getCategorias() {
        return ResponseEntity.ok(getCatalogUseCase.getCategorias());
    }

    @GetMapping("/categories/{categoriaId}/subcategories")
    public ResponseEntity<?> getSubcategorias(@PathVariable UUID categoriaId) {
        return ResponseEntity.ok(getCatalogUseCase.getSubcategoriasByCategoria(categoriaId));
    }
}