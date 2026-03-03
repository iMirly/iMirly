package tfg.imirly.contracting.infrastructure.output.persistence;

import org.springframework.stereotype.Component;
import tfg.imirly.auth.infrastructure.output.persistence.entity.UserEntity;
import tfg.imirly.catalog.infrastructure.output.persistence.entity.AnuncioServicioEntity;
import tfg.imirly.contracting.domain.model.SolicitudContrato;
import tfg.imirly.contracting.infrastructure.output.persistence.entity.SolicitudContratoEntity;

@Component
public class SolicitudContratoMapper {

    public SolicitudContrato toDomain(SolicitudContratoEntity entity) {
        // Ajustamos al constructor generado por @AllArgsConstructor en el dominio
        // El orden debe ser: id, clienteId, proveedorId, anuncioId, estado, detalles, precio, fecha, nombreProveedor
        return new SolicitudContrato(
                entity.getId(),
                entity.getCliente().getId(),
                entity.getAnuncio().getProveedor().getId(), // proveedorId
                entity.getAnuncio().getId(),                // anuncioId
                entity.getEstado(),
                entity.getDetallesSolicitud(),
                entity.getPrecioAcordado(),                 // <--- NUEVO
                entity.getFechaCreacion(),
                entity.getAnuncio().getProveedor().getNombre(),
                entity.getCliente().getNombre()
        );
    }

    public SolicitudContratoEntity toEntity(SolicitudContrato domain) {
        SolicitudContratoEntity entity = new SolicitudContratoEntity();
        
        if (domain.getId() != null) {
            entity.setId(domain.getId());
        }
        
        entity.setEstado(domain.getEstado());
        entity.setDetallesSolicitud(domain.getDetallesSolicitud());
        entity.setFechaCreacion(domain.getFechaCreacion());
        entity.setPrecioAcordado(domain.getPrecioAcordado()); // <--- NUEVO

        // Mapeo de claves foráneas mediante instancias "proxy"
        UserEntity cliente = new UserEntity();
        cliente.setId(domain.getClienteId());
        entity.setCliente(cliente);

        AnuncioServicioEntity anuncio = new AnuncioServicioEntity();
        anuncio.setId(domain.getAnuncioId());
        entity.setAnuncio(anuncio);

        return entity;
    }
}