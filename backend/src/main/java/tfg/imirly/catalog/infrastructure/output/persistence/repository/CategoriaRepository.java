package tfg.imirly.catalog.infrastructure.output.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tfg.imirly.catalog.infrastructure.output.persistence.entity.CategoriaEntity;
import java.util.UUID;

import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface CategoriaRepository extends JpaRepository<CategoriaEntity, UUID> {

    @Query("SELECT c.id, c.nombre, c.icono, COUNT(a.id) " +
            "FROM CategoriaEntity c " +
            "LEFT JOIN SubcategoriaEntity s ON s.categoria.id = c.id " +
            "LEFT JOIN AnuncioServicioEntity a ON a.subcategoria.id = s.id " +
            "GROUP BY c.id, c.nombre, c.icono")
    List<Object[]> findAllWithAnunciosCount();
}