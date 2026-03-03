package tfg.imirly.auth.infrastructure.output.persistence.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.UUID;
import java.time.LocalDateTime;
import tfg.imirly.auth.domain.model.UserType;

@Entity
@Table(name = "usuarios")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String nombre;
    private String email;

    @Column(name = "password_hash")
    private String passwordHash;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_usuario")
    private UserType tipo;

    @Column(name = "documento_identidad")
    private String documentoIdentidad;

    private boolean verificado;

    @Column(name = "fecha_registro")
    private LocalDateTime fechaRegistro;

    @Column(name = "saldo", columnDefinition = "DECIMAL(10,2) DEFAULT 0.0")
    private Double saldo;

    @Column(name = "puntuacion_total", columnDefinition = "DOUBLE PRECISION DEFAULT 0.0")
    private Double puntuacionTotal = 0.0;

    @Column(name = "numero_valoraciones", columnDefinition = "INTEGER DEFAULT 0")
    private Integer numeroValoraciones = 0;
}