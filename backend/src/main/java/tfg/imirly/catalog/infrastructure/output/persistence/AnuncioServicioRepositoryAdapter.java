package tfg.imirly.catalog.infrastructure.output.persistence;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import tfg.imirly.catalog.domain.model.AnuncioServicio;
import tfg.imirly.catalog.domain.port.out.AnuncioServicioRepositoryPort;
import tfg.imirly.catalog.infrastructure.output.persistence.entity.AnuncioServicioEntity;
import tfg.imirly.catalog.infrastructure.output.persistence.repository.AnuncioServicioRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class AnuncioServicioRepositoryAdapter implements AnuncioServicioRepositoryPort {

    private final AnuncioServicioRepository springDataRepository;
    private final AnuncioServicioMapper mapper;

    @Override
    public AnuncioServicio save(AnuncioServicio anuncio) {
        AnuncioServicioEntity entity = mapper.toEntity(anuncio);
        return mapper.toDomain(springDataRepository.save(entity));
    }

    @Override
    public Optional<AnuncioServicio> findById(UUID id) {
        return springDataRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    public List<AnuncioServicio> findBySubcategoriaId(UUID subcategoriaId) {
        return springDataRepository.findBySubcategoriaIdAndActivoTrue(subcategoriaId).stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(UUID id) {
        springDataRepository.deleteById(id);
    }

    @Override
    public List<AnuncioServicio> findByProveedorId(UUID proveedorId) {
        return springDataRepository.findByProveedorId(proveedorId).stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<AnuncioServicio> searchAnuncios(String query, String ubicacion, UUID categoriaId) {
        // TRUCO ANTICRASH: Convertimos nulls a textos vacíos para PostgreSQL
        String safeQuery = (query == null) ? "" : query;
        String safeUbicacion = (ubicacion == null) ? "" : ubicacion;

        // Le pasamos el categoriaId al repositorio de Spring Data
        return springDataRepository.searchAnuncios(safeQuery, safeUbicacion, categoriaId).stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }
}