package tfg.imirly.catalog.domain.model;

import java.util.UUID;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Categoria {
    private UUID id;
    private String nombre;
    private String icono;
    private Integer numAnuncios;
}