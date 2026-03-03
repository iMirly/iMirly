package tfg.imirly.catalog.infrastructure.output.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import tfg.imirly.catalog.infrastructure.output.persistence.entity.AnuncioServicioEntity;
import java.util.List;
import java.util.UUID;

public interface AnuncioServicioRepository extends JpaRepository<AnuncioServicioEntity, UUID> {
    List<AnuncioServicioEntity> findBySubcategoriaIdAndActivoTrue(UUID subcategoriaId);
    List<AnuncioServicioEntity> findByProveedorId(UUID proveedorId);
    @Query("SELECT a FROM AnuncioServicioEntity a WHERE a.activo = true " +
           "AND (:query = '' OR LOWER(a.titulo) LIKE LOWER(CONCAT('%', :query, '%')) " +
           "OR LOWER(a.descripcion) LIKE LOWER(CONCAT('%', :query, '%'))) " +
           "AND (:ubicacion = '' OR LOWER(a.ubicacion) LIKE LOWER(CONCAT('%', :ubicacion, '%'))) " +
           "AND (CAST(:categoriaId AS org.hibernate.type.UUIDCharType) IS NULL OR a.subcategoria.id IN (SELECT s.id FROM SubcategoriaEntity s WHERE s.categoria.id = :categoriaId))")
    List<AnuncioServicioEntity> searchAnuncios(@Param("query") String query, @Param("ubicacion") String ubicacion, @Param("categoriaId") UUID categoriaId);
}