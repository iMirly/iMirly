package tfg.imirly.messages.infrastructure.output.persistence.entity;

import jakarta.persistence.*;
import lombok.*;
import tfg.imirly.auth.infrastructure.output.persistence.entity.UserEntity;
import tfg.imirly.messages.domain.model.TipoMensaje;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "mensaje")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MensajeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "remitente_id", nullable = false)
    private UserEntity remitente;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receptor_id", nullable = false)
    private UserEntity receptor;

    // Guardamos el ID de la solicitud para agrupar mensajes por contrato
    @Column(name = "solicitud_id")
    private UUID solicitudId;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String contenido;

    // Guardamos el Enum como String en la base de datos para que sea legible
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoMensaje tipo;

    @Column(nullable = false)
    private LocalDateTime timestamp;

    @Column(nullable = false)
    private boolean leido;
}