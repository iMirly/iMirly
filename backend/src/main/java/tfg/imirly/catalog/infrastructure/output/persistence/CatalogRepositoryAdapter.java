package tfg.imirly.catalog.infrastructure.output.persistence;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import tfg.imirly.catalog.domain.model.Categoria;
import tfg.imirly.catalog.domain.model.Subcategoria;
import tfg.imirly.catalog.domain.port.out.CategoriaRepositoryPort;
import tfg.imirly.catalog.domain.port.out.SubcategoriaRepositoryPort;
import tfg.imirly.catalog.infrastructure.output.persistence.repository.CategoriaRepository;
import tfg.imirly.catalog.infrastructure.output.persistence.repository.SubcategoriaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class CatalogRepositoryAdapter implements CategoriaRepositoryPort, SubcategoriaRepositoryPort {

    private final CategoriaRepository categoriaRepository;
    private final SubcategoriaRepository subcategoriaRepository;
    private final CategoriaMapper categoriaMapper;
    private final SubcategoriaMapper subcategoriaMapper;

    @Override
    public List<Categoria> findAll() {
        return categoriaRepository.findAllWithAnunciosCount().stream()
                .map(obj -> {
                    UUID id = (UUID) obj[0];
                    String nombre = (String) obj[1];
                    String icono = CategoriaMapper.checkLocalIcon((String) obj[2]);
                    Integer count = ((Number) obj[3]).intValue();
                    return new Categoria(id, nombre, icono, count);
                })
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Categoria> findById(UUID id) {
        return categoriaRepository.findById(id).map(categoriaMapper::toDomain);
    }

    @Override
    public List<Subcategoria> findByCategoriaId(UUID categoriaId) {
        // Asume que en SubcategoriaRepository añadiste: List<SubcategoriaEntity>
        // findByCategoriaId(UUID categoriaId);
        return subcategoriaRepository.findByCategoriaId(categoriaId).stream()
                .map(subcategoriaMapper::toDomain)
                .collect(Collectors.toList());
    }
}