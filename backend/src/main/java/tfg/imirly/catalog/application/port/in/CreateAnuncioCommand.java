package tfg.imirly.catalog.application.port.in;

import lombok.AllArgsConstructor;
import lombok.Getter;
import java.math.BigDecimal;
import java.util.UUID;

@Getter
@AllArgsConstructor
public class CreateAnuncioCommand {
    private UUID proveedorId;
    private UUID subcategoriaId;
    private String titulo;
    private String descripcion;
    private BigDecimal precioHora;
    private String ubicacion;
}