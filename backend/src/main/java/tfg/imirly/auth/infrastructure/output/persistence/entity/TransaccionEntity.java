package tfg.imirly.auth.infrastructure.output.persistence.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "transacciones")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TransaccionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "usuario_id", nullable = false)
    private UUID usuarioId;

    @Column(nullable = false)
    private Double cantidad;

    @Column(nullable = false)
    private String tipo; // "INGRESO", "RETIRO", "SERVICIO_COBRADO", "SERVICIO_PAGADO"

    @Column(nullable = false)
    private String descripcion; // ej. "Servicio de fontanería" o "Ingreso de saldo"

    @Column(nullable = false)
    private LocalDateTime fecha;
}
