package tfg.imirly.catalog.infrastructure.input.rest.dto;

import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;

@Getter
@Setter
public class UpdateAnuncioRequest {
    private String titulo;
    private String descripcion;
    private BigDecimal precioHora;
    private String ubicacion;
    private Boolean activo;
}