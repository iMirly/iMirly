package tfg.imirly.contracting.infrastructure.output.persistence.entity;

import jakarta.persistence.*;
import lombok.*;
import tfg.imirly.auth.infrastructure.output.persistence.entity.UserEntity;
import tfg.imirly.catalog.infrastructure.output.persistence.entity.AnuncioServicioEntity;
import tfg.imirly.contracting.domain.model.EstadoSolicitud;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "solicitud_contrato")
@Getter 
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SolicitudContratoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "cliente_id", nullable = false)
    private UserEntity cliente;

    @ManyToOne
    @JoinColumn(name = "anuncio_id", nullable = false)
    private AnuncioServicioEntity anuncio;

    @Enumerated(EnumType.STRING)
    private EstadoSolicitud estado;

    @Column(name = "detalles_solicitud", columnDefinition = "TEXT")
    private String detallesSolicitud;

    @Column(name = "precio_acordado")
    private Double precioAcordado;

    @Column(name = "fecha_creacion")
    private LocalDateTime fechaCreacion;
}