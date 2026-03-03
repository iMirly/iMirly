package tfg.imirly.contracting.infrastructure.output.persistence;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import tfg.imirly.contracting.domain.model.SolicitudContrato;
import tfg.imirly.contracting.domain.port.out.SolicitudContratoRepositoryPort;
import tfg.imirly.contracting.infrastructure.output.persistence.entity.SolicitudContratoEntity;
import tfg.imirly.contracting.infrastructure.output.persistence.repository.SolicitudContratoRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class SolicitudContratoRepositoryAdapter implements SolicitudContratoRepositoryPort {

    private final SolicitudContratoRepository springDataRepository;
    private final SolicitudContratoMapper mapper;

    @Override
    public SolicitudContrato save(SolicitudContrato solicitud) {
        SolicitudContratoEntity entity = mapper.toEntity(solicitud);
        return mapper.toDomain(springDataRepository.save(entity));
    }

    @Override
    public Optional<SolicitudContrato> findById(UUID id) {
        return springDataRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    public List<SolicitudContrato> findByClienteId(UUID clienteId) {
        return springDataRepository.findByClienteId(clienteId).stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<SolicitudContrato> findByProveedorId(UUID proveedorId) {
        return springDataRepository.findByAnuncioProveedorId(proveedorId).stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(UUID id) {
        springDataRepository.deleteById(id);
    }

    @Override
    @org.springframework.transaction.annotation.Transactional
    public void borrarSolicitudesEntreUsuarios(UUID usuario1, UUID usuario2) {
        springDataRepository.borrarSolicitudesEntreUsuarios(usuario1, usuario2);
    }
}