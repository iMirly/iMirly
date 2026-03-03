package tfg.imirly.messages.infrastructure.output.persistence;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import tfg.imirly.messages.domain.model.Mensaje;
import tfg.imirly.messages.domain.port.out.MensajeRepositoryPort;
import tfg.imirly.messages.infrastructure.output.persistence.repository.MensajeRepository;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class MensajeRepositoryAdapter implements MensajeRepositoryPort {

    private final MensajeRepository springDataRepository;
    private final MensajeMapper mapper;

    @Override
    public Mensaje save(Mensaje mensaje) {
        var entity = mapper.toEntity(mensaje);
        return mapper.toDomain(springDataRepository.save(entity));
    }

    @Override
    public List<Mensaje> findConversacion(UUID usuario1Id, UUID usuario2Id) {
        return springDataRepository.findConversacion(usuario1Id, usuario2Id)
                .stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional // Obligatorio porque es una consulta UPDATE (Modifying)
    public void marcarMensajesComoLeidos(UUID remitenteId, UUID receptorId) {
        springDataRepository.marcarMensajesComoLeidos(remitenteId, receptorId);
    }

    @Override
    @Transactional
    public void borrarConversacion(UUID usuario1Id, UUID usuario2Id) {
        springDataRepository.borrarConversacion(usuario1Id, usuario2Id);
    }
}