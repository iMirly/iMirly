package tfg.imirly.catalog.infrastructure.input.rest.dto;

import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
public class CreateAnuncioRequest {
    private UUID subcategoriaId;
    private String titulo;
    private String descripcion;
    private BigDecimal precioHora;
    private String ubicacion;
}