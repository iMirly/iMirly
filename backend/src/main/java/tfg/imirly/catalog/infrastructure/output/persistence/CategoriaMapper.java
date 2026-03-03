package tfg.imirly.catalog.infrastructure.output.persistence;

import org.springframework.stereotype.Component;
import tfg.imirly.catalog.domain.model.Categoria;
import tfg.imirly.catalog.infrastructure.output.persistence.entity.CategoriaEntity;

@Component
public class CategoriaMapper {
    public Categoria toDomain(CategoriaEntity entity) {
        return new Categoria(entity.getId(), entity.getNombre(), checkLocalIcon(entity.getIcono()), 0);
    }

    public static String checkLocalIcon(String icon) {
        if (icon == null)
            return "otros";
        switch (icon) {
            case "fitness_center":
                return "deporte";
            case "pets":
                return "mascotas";
            case "home":
                return "hogar";
            case "school":
                return "clases";
            case "face":
                return "belleza";
            case "computer":
                return "otros";
            case "camera":
                return "otros";
            case "spa":
                return "cuidado";
            default:
                return icon;
        }
    }
}