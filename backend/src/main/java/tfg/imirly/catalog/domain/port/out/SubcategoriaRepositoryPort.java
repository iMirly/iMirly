package tfg.imirly.catalog.domain.port.out;

import tfg.imirly.catalog.domain.model.Subcategoria;
import java.util.List;
import java.util.UUID;

public interface SubcategoriaRepositoryPort {
    List<Subcategoria> findByCategoriaId(UUID categoriaId);
}