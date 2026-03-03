package tfg.imirly.catalog.infrastructure.output.persistence.entity;

import jakarta.persistence.*;
import tfg.imirly.auth.infrastructure.output.persistence.entity.UserEntity;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "anuncio_servicio")
public class AnuncioServicioEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "proveedor_id", nullable = false)
    private UserEntity proveedor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subcategoria_id", nullable = false)
    private SubcategoriaEntity subcategoria;

    @Column(nullable = false, length = 150)
    private String titulo;

    @Column(columnDefinition = "TEXT")
    private String descripcion;

    @Column(name = "precio_hora", nullable = false, precision = 10, scale = 2)
    private BigDecimal precioHora;

    private String ubicacion;

    @Column(nullable = false)
    private Boolean activo = true;

    // Getters y Setters
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public UserEntity getProveedor() { return proveedor; }
    public void setProveedor(UserEntity proveedor) { this.proveedor = proveedor; }
    public SubcategoriaEntity getSubcategoria() { return subcategoria; }
    public void setSubcategoria(SubcategoriaEntity subcategoria) { this.subcategoria = subcategoria; }
    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public BigDecimal getPrecioHora() { return precioHora; }
    public void setPrecioHora(BigDecimal precioHora) { this.precioHora = precioHora; }
    public String getUbicacion() { return ubicacion; }
    public void setUbicacion(String ubicacion) { this.ubicacion = ubicacion; }
    public Boolean getActivo() { return activo; }
    public void setActivo(Boolean activo) { this.activo = activo; }
}