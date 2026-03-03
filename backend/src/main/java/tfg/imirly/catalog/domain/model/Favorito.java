package tfg.imirly.catalog.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Favorito {
    private UUID id;
    private UUID usuarioId;
    private UUID anuncioId;
    private LocalDateTime fechaGuardado;

    // Constructor para cuando creamos uno nuevo (sin ID ni fecha aún)
    public Favorito(UUID usuarioId, UUID anuncioId) {
        this.usuarioId = usuarioId;
        this.anuncioId = anuncioId;
    }
}