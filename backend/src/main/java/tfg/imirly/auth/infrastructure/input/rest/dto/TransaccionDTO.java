package tfg.imirly.auth.infrastructure.input.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TransaccionDTO {
    private String titulo;
    private String subTitulo;
    private String cantidadFormateada;
    private String estado;
    private String tiempo;
}
