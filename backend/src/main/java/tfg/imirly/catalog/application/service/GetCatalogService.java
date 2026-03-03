package tfg.imirly.catalog.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tfg.imirly.catalog.application.port.in.GetCatalogUseCase;
import tfg.imirly.catalog.domain.model.AnuncioServicio;
import tfg.imirly.catalog.domain.model.Categoria;
import tfg.imirly.catalog.domain.model.Subcategoria;
import tfg.imirly.catalog.domain.port.out.AnuncioServicioRepositoryPort;
import tfg.imirly.catalog.domain.port.out.CategoriaRepositoryPort;
import tfg.imirly.catalog.domain.port.out.SubcategoriaRepositoryPort;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GetCatalogService implements GetCatalogUseCase {

    private final CategoriaRepositoryPort categoriaRepositoryPort;
    private final SubcategoriaRepositoryPort subcategoriaRepositoryPort;
    private final AnuncioServicioRepositoryPort anuncioRepositoryPort; // <--- Añadido

    @Override
    public List<Categoria> getCategorias() {
        return categoriaRepositoryPort.findAll();
    }

    @Override
    public List<Subcategoria> getSubcategoriasByCategoria(UUID categoriaId) {
        return subcategoriaRepositoryPort.findByCategoriaId(categoriaId);
    }

    // --- NUEVOS MÉTODOS PARA CONSULTAR ANUNCIOS ---
    @Override
    public List<AnuncioServicio> getAnunciosBySubcategoria(UUID subcategoriaId) {
        return anuncioRepositoryPort.findBySubcategoriaId(subcategoriaId);
    }

    @Override
    public AnuncioServicio getAnuncioById(UUID anuncioId) {
        return anuncioRepositoryPort.findById(anuncioId)
                .orElseThrow(() -> new IllegalArgumentException("Anuncio no encontrado"));
    }

    @Override
    public List<AnuncioServicio> getAnunciosByProveedor(UUID proveedorId) {
        return anuncioRepositoryPort.findByProveedorId(proveedorId);
    }

    @Override
    public List<AnuncioServicio> searchAnuncios(String query, String ubicacion, UUID categoriaId) {
        return anuncioRepositoryPort.searchAnuncios(query, ubicacion, categoriaId);
    }
}