package tfg.imirly.auth.infrastructure.output.persistence;

import org.springframework.stereotype.Component;
import tfg.imirly.auth.domain.model.User;
import tfg.imirly.auth.infrastructure.output.persistence.entity.UserEntity;

@Component
public class UserMapper {

    // Convierte el Dominio en Entidad (Para guardar en BD)
    public UserEntity toEntity(User user) {
        return new UserEntity(
                user.getId(), user.getNombre(), user.getEmail(),
                user.getPasswordHash(), user.getTipo(), user.getDocumentoIdentidad(),
                user.isVerificado(), user.getFechaRegistro(),
                user.getSaldo(), user.getPuntuacionTotal(), user.getNumeroValoraciones());
    }

    // Convierte la Entidad en Dominio (Para devolver al caso de uso)
    public User toDomain(UserEntity entity) {
        User user = new User();
        user.setId(entity.getId());
        user.setNombre(entity.getNombre());
        user.setEmail(entity.getEmail());
        user.setPasswordHash(entity.getPasswordHash());
        user.setTipo(entity.getTipo());
        user.setDocumentoIdentidad(entity.getDocumentoIdentidad());
        user.setVerificado(entity.isVerificado());
        user.setFechaRegistro(entity.getFechaRegistro());
        user.setSaldo(entity.getSaldo());
        user.setPuntuacionTotal(entity.getPuntuacionTotal());
        user.setNumeroValoraciones(entity.getNumeroValoraciones());
        return user;
    }
}