package tfg.imirly.catalog.domain.port.out;

import tfg.imirly.catalog.domain.model.Categoria;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CategoriaRepositoryPort {
    List<Categoria> findAll();
    Optional<Categoria> findById(UUID id);
}