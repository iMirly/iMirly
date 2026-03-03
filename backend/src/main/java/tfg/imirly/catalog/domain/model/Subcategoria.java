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
public class Subcategoria {
    private UUID id;
    private UUID categoriaId; // Referencia a la categoría padre
    private String nombre;
    private String icono;
}