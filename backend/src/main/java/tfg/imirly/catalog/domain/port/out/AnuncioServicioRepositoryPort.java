package tfg.imirly.catalog.domain.port.out;

import tfg.imirly.catalog.domain.model.AnuncioServicio;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AnuncioServicioRepositoryPort {
    AnuncioServicio save(AnuncioServicio anuncio);
    Optional<AnuncioServicio> findById(UUID id);
    List<AnuncioServicio> findBySubcategoriaId(UUID subcategoriaId);
    List<AnuncioServicio> findByProveedorId(UUID proveedorId);
    void deleteById(UUID id);
    List<AnuncioServicio> searchAnuncios(String query, String ubicacion, UUID categoriaId);}