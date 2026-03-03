package tfg.imirly.catalog.infrastructure.output.persistence.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.UUID;
import java.time.LocalDateTime;

@Entity
@Table(name = "favoritos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FavoritoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "usuario_id", nullable = false)
    private UUID usuarioId;

    @Column(name = "anuncio_id", nullable = false)
    private UUID anuncioId;

    @Column(name = "fecha_guardado")
    private LocalDateTime fechaGuardado = LocalDateTime.now();
}