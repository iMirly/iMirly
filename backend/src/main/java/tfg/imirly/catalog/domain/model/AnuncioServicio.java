package tfg.imirly.catalog.domain.model;

import java.math.BigDecimal;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AnuncioServicio {
    private UUID id;
    private UUID proveedorId; // Referencia al User (Módulo auth)
    private UUID subcategoriaId; // Referencia a la subcategoría
    private String titulo;
    private String descripcion;
    private BigDecimal precioHora;
    private String ubicacion;
    private boolean activo;
    private String nombreProfesional;
    private Double valoracionProfesional;
    private Integer numeroValoracionesProfesional;

    // Constructor personalizado para cuando un profesional crea un anuncio nuevo
    public AnuncioServicio(UUID proveedorId, UUID subcategoriaId, String titulo, String descripcion,
            BigDecimal precioHora, String ubicacion) {
        this.proveedorId = proveedorId;
        this.subcategoriaId = subcategoriaId;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.precioHora = precioHora;
        this.ubicacion = ubicacion;
        this.activo = true; // Por defecto nace activo
        this.valoracionProfesional = 0.0;
        this.numeroValoracionesProfesional = 0;
    }

    // Lógica de negocio pura
    public void pausarAnuncio() {
        this.activo = false;
    }

    public void reactivarAnuncio() {
        this.activo = true;
    }
}