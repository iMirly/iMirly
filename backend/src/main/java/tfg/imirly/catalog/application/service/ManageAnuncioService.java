package tfg.imirly.catalog.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tfg.imirly.catalog.application.port.in.CreateAnuncioCommand;
import tfg.imirly.catalog.application.port.in.ManageAnuncioUseCase;
import tfg.imirly.catalog.application.port.in.UpdateAnuncioCommand;
import tfg.imirly.catalog.domain.model.AnuncioServicio;
import tfg.imirly.catalog.domain.port.out.AnuncioServicioRepositoryPort;
import tfg.imirly.messages.application.service.ProfanityFilterService;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ManageAnuncioService implements ManageAnuncioUseCase {

    private final AnuncioServicioRepositoryPort anuncioRepositoryPort;
    private final ProfanityFilterService profanityFilterService;

    @Override
    public AnuncioServicio publicarAnuncio(CreateAnuncioCommand command) {
        // Validar que el título y la descripción no tengan insultos
        if (profanityFilterService.containsProfanity(command.getTitulo()) || 
            profanityFilterService.containsProfanity(command.getDescripcion())) {
            throw new IllegalArgumentException("El anuncio contiene lenguaje no permitido.");
        }

        // Usamos el constructor de dominio que creamos antes
        AnuncioServicio nuevoAnuncio = new AnuncioServicio(
                command.getProveedorId(),
                command.getSubcategoriaId(),
                command.getTitulo(),
                command.getDescripcion(),
                command.getPrecioHora(),
                command.getUbicacion()
        );
        
        return anuncioRepositoryPort.save(nuevoAnuncio);
    }

    @Override
    public AnuncioServicio modificarAnuncio(UpdateAnuncioCommand command) {
        // 1. Buscamos el anuncio
        AnuncioServicio anuncio = anuncioRepositoryPort.findById(command.getAnuncioId())
                .orElseThrow(() -> new IllegalArgumentException("Anuncio no encontrado"));

        // 2. Seguridad: Verificamos que el usuario que edita sea el dueño
        if (!anuncio.getProveedorId().equals(command.getProveedorId())) {
            throw new SecurityException("No tienes permiso para editar este anuncio");
        }

        // 3. Validar contenido inapropiado antes de actualizar
        if (profanityFilterService.containsProfanity(command.getTitulo()) || 
            profanityFilterService.containsProfanity(command.getDescripcion())) {
            throw new IllegalArgumentException("El contenido actualizado contiene lenguaje no permitido.");
        }

        // 4. Actualizamos los campos
        anuncio.setTitulo(command.getTitulo());
        anuncio.setDescripcion(command.getDescripcion());
        anuncio.setPrecioHora(command.getPrecioHora());
        anuncio.setUbicacion(command.getUbicacion());
        anuncio.setActivo(command.getActivo());

        // 5. Guardamos
        return anuncioRepositoryPort.save(anuncio);
    }

    @Override
    public void eliminarAnuncio(UUID anuncioId, UUID proveedorId) {
        AnuncioServicio anuncio = anuncioRepositoryPort.findById(anuncioId)
                .orElseThrow(() -> new IllegalArgumentException("Anuncio no encontrado"));

        if (!anuncio.getProveedorId().equals(proveedorId)) {
            throw new SecurityException("No tienes permiso para eliminar este anuncio");
        }

        anuncioRepositoryPort.deleteById(anuncioId);
    }
}
