package tfg.imirly.catalog.application.port.in;

import tfg.imirly.catalog.domain.model.AnuncioServicio;
import tfg.imirly.catalog.domain.model.Categoria;
import tfg.imirly.catalog.domain.model.Subcategoria;
import java.util.List;
import java.util.UUID;

public interface GetCatalogUseCase {
    List<Categoria> getCategorias();
    List<Subcategoria> getSubcategoriasByCategoria(UUID categoriaId);
    List<AnuncioServicio> getAnunciosBySubcategoria(UUID subcategoriaId);
    AnuncioServicio getAnuncioById(UUID anuncioId);
    List<AnuncioServicio> getAnunciosByProveedor(UUID proveedorId);

    // Añadir:
    List<AnuncioServicio> searchAnuncios(String query, String ubicacion, UUID categoriaId);
}