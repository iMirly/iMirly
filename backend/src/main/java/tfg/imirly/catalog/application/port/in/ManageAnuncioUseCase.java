package tfg.imirly.catalog.application.port.in;

import tfg.imirly.catalog.domain.model.AnuncioServicio;
import java.util.UUID;

public interface ManageAnuncioUseCase {
    AnuncioServicio publicarAnuncio(CreateAnuncioCommand command);
    AnuncioServicio modificarAnuncio(UpdateAnuncioCommand command);
    void eliminarAnuncio(UUID anuncioId, UUID proveedorId);
}