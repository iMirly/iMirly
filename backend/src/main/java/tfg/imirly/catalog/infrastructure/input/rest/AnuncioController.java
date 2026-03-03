package tfg.imirly.catalog.infrastructure.input.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tfg.imirly.catalog.application.port.in.CreateAnuncioCommand;
import tfg.imirly.catalog.application.port.in.GetCatalogUseCase;
import tfg.imirly.catalog.application.port.in.ManageAnuncioUseCase;
import tfg.imirly.catalog.application.port.in.UpdateAnuncioCommand;
import tfg.imirly.catalog.infrastructure.input.rest.dto.CreateAnuncioRequest;
import tfg.imirly.catalog.infrastructure.input.rest.dto.UpdateAnuncioRequest;
import tfg.imirly.auth.application.port.in.GetUserUseCase;
import tfg.imirly.auth.domain.model.User;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/anuncios")
@RequiredArgsConstructor
public class AnuncioController {

    private final ManageAnuncioUseCase manageAnuncioUseCase;
    private final GetCatalogUseCase getCatalogUseCase;
    private final GetUserUseCase getUserUseCase;


    // --- 1. CONSULTAR TODOS LOS ANUNCIOS DE UNA SUBCATEGORÍA ---
    @GetMapping("/subcategoria/{subcategoriaId}")
    public ResponseEntity<?> getAnunciosBySubcategoria(@PathVariable UUID subcategoriaId) {
        return ResponseEntity.ok(getCatalogUseCase.getAnunciosBySubcategoria(subcategoriaId));
    }

    // --- 2. CONSULTAR UN ANUNCIO ESPECÍFICO ---
    @GetMapping("/{id}")
    public ResponseEntity<?> getAnuncioById(@PathVariable UUID id) {
        try {
            return ResponseEntity.ok(getCatalogUseCase.getAnuncioById(id));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/mis-anuncios")
    public ResponseEntity<?> getMisAnuncios() {
        try {
            // 1. Sacamos el email del Token
            String email = org.springframework.security.core.context.SecurityContextHolder
                                .getContext().getAuthentication().getName();

            // 2. Buscamos tu ID real en la base de datos
            User user = getUserUseCase.getUserByEmail(email);

            // 3. Devolvemos tus anuncios
            return ResponseEntity.ok(getCatalogUseCase.getAnunciosByProveedor(user.getId()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/search")
    public ResponseEntity<?> searchAnuncios(
            @RequestParam(required = false) String q,
            @RequestParam(required = false) String ubicacion,
            @RequestParam(required = false) UUID categoriaId) { // <--- AÑADIDO
        return ResponseEntity.ok(getCatalogUseCase.searchAnuncios(q, ubicacion, categoriaId));
    }

    // --- 3. CREAR ANUNCIO ---
    @PostMapping
    public ResponseEntity<?> crearAnuncio(@RequestBody CreateAnuncioRequest request) {
        try {
            String tokenUserId = (String) org.springframework.security.core.context.SecurityContextHolder
                                    .getContext().getAuthentication().getCredentials();
            UUID proveedorId = UUID.fromString(tokenUserId);

            CreateAnuncioCommand command = new CreateAnuncioCommand(
                    proveedorId, request.getSubcategoriaId(), request.getTitulo(),
                    request.getDescripcion(), request.getPrecioHora(), request.getUbicacion()
            );

            return ResponseEntity.status(HttpStatus.CREATED).body(manageAnuncioUseCase.publicarAnuncio(command));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // --- 4. EDITAR ANUNCIO ---
    @PutMapping("/{id}")
    public ResponseEntity<?> editarAnuncio(@PathVariable UUID id, @RequestBody UpdateAnuncioRequest request) {
        try {
            String tokenUserId = (String) org.springframework.security.core.context.SecurityContextHolder
                                    .getContext().getAuthentication().getCredentials();
            UUID proveedorId = UUID.fromString(tokenUserId);

            UpdateAnuncioCommand command = new UpdateAnuncioCommand(
                    id, proveedorId, request.getTitulo(), request.getDescripcion(),
                    request.getPrecioHora(), request.getUbicacion(), request.getActivo()
            );

            return ResponseEntity.ok(manageAnuncioUseCase.modificarAnuncio(command));
        } catch (SecurityException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // --- 5. ELIMINAR ANUNCIO ---
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarAnuncio(@PathVariable UUID id) {
        try {
            String tokenUserId = (String) org.springframework.security.core.context.SecurityContextHolder
                                    .getContext().getAuthentication().getCredentials();
            UUID proveedorId = UUID.fromString(tokenUserId);

            manageAnuncioUseCase.eliminarAnuncio(id, proveedorId);
            return ResponseEntity.ok("Anuncio eliminado correctamente");
        } catch (SecurityException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}