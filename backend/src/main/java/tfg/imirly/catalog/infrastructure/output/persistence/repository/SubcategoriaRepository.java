package tfg.imirly.catalog.infrastructure.output.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tfg.imirly.catalog.infrastructure.output.persistence.entity.SubcategoriaEntity;

import java.util.List;
import java.util.UUID;

public interface SubcategoriaRepository extends JpaRepository<SubcategoriaEntity, UUID> {
    
    // Spring Boot traduce esto automáticamente a: 
    // SELECT * FROM subcategoria WHERE categoria_id = ?
    List<SubcategoriaEntity> findByCategoriaId(UUID categoriaId);
}