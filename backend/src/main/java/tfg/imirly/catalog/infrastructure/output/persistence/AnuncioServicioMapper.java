package tfg.imirly.catalog.infrastructure.output.persistence;

import org.springframework.stereotype.Component;

import tfg.imirly.auth.infrastructure.output.persistence.entity.UserEntity;
import tfg.imirly.catalog.domain.model.AnuncioServicio;
import tfg.imirly.catalog.infrastructure.output.persistence.entity.AnuncioServicioEntity;
import tfg.imirly.catalog.infrastructure.output.persistence.entity.SubcategoriaEntity;

@Component
public class AnuncioServicioMapper {

    public AnuncioServicio toDomain(AnuncioServicioEntity entity) {
        Double total = entity.getProveedor().getPuntuacionTotal();
        Integer cant = entity.getProveedor().getNumeroValoraciones();
        Double media = (cant != null && cant > 0) ? Math.round((total / cant) * 10.0) / 10.0 : 0.0;

        return new AnuncioServicio(
                entity.getId(),
                entity.getProveedor().getId(),
                entity.getSubcategoria().getId(),
                entity.getTitulo(),
                entity.getDescripcion(),
                entity.getPrecioHora(),
                entity.getUbicacion(),
                entity.getActivo(),
                entity.getProveedor().getNombre(),
                media,
                cant == null ? 0 : cant);
    }

    public AnuncioServicioEntity toEntity(AnuncioServicio domain) {
        AnuncioServicioEntity entity = new AnuncioServicioEntity();
        if (domain.getId() != null)
            entity.setId(domain.getId());

        entity.setTitulo(domain.getTitulo());
        entity.setDescripcion(domain.getDescripcion());
        entity.setPrecioHora(domain.getPrecioHora());
        entity.setUbicacion(domain.getUbicacion());
        entity.setActivo(domain.isActivo());

        // Truco JPA: Solo necesitamos instanciar con el ID para guardar la relación
        UserEntity proveedor = new UserEntity();
        proveedor.setId(domain.getProveedorId());
        entity.setProveedor(proveedor);

        SubcategoriaEntity subcategoria = new SubcategoriaEntity();
        subcategoria.setId(domain.getSubcategoriaId());
        entity.setSubcategoria(subcategoria);

        return entity;
    }
}