package tfg.imirly.auth.infrastructure.input.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import java.util.List;

@Getter
@AllArgsConstructor
public class SaldoResponse {
    private Double disponible;
    private Double pendiente;
    private Double esteMes;
    private List<TransaccionDTO> recientes;
}
