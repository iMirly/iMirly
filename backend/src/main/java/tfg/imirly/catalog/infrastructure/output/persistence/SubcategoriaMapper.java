package tfg.imirly.catalog.infrastructure.output.persistence;

import org.springframework.stereotype.Component;
import tfg.imirly.catalog.domain.model.Subcategoria;
import tfg.imirly.catalog.infrastructure.output.persistence.entity.SubcategoriaEntity;

@Component
public class SubcategoriaMapper {
    public Subcategoria toDomain(SubcategoriaEntity entity) {
        return new Subcategoria(entity.getId(), entity.getCategoria().getId(), entity.getNombre(),
                mapSubcategoriaIcon(entity.getNombre(), CategoriaMapper.checkLocalIcon(entity.getIcono())));
    }

    private String mapSubcategoriaIcon(String nombre, String fallbackIcon) {
        if (nombre == null)
            return fallbackIcon;
        switch (nombre) {
            // Mascotas
            case "Adiestramiento":
                return "mascotas_adiestrador";
            case "Conducta":
                return "mascotas_conducta";
            case "Cuidador":
            case "Cuidado a Domicilio":
                return "mascotas_cuidador";
            case "Paseador":
            case "Paseador de Perros":
                return "mascotas_paseador";
            case "Peluquería Canina":
                return "mascotas_peluqueria";

            // Deporte
            case "Boxeo":
                return "deporte_boxeo";
            case "Entrenador personal":
            case "Entrenador Personal":
                return "deporte_entrenador_personal";
            case "Pádel":
                return "deporte_padel";
            case "Pilates":
            case "Yoga y Pilates":
                return "deporte_pilates";
            case "Tenis":
                return "deporte_tenis";
            case "Yoga":
                return "deporte_yoga";

            // Belleza
            case "Depilación":
                return "belleza_depilacion";
            case "Facial":
            case "Tratamientos Faciales":
                return "belleza_facial";
            case "Maquillaje":
                return "belleza_maquillaje";
            case "Masajes":
                return "belleza_masajes";
            case "Peluquería":
                return "belleza_peluqueria";
            case "Uñas":
            case "Manicura y Pedicura":
                return "belleza_unas";

            // Hogar
            case "Electricista":
            case "Electricidad":
                return "hogar_electricista";
            case "Electrodomésticos":
                return "hogar_electrodomesticos";
            case "Fontanería":
                return "hogar_fontaneria";
            case "Jardinería":
                return "hogar_jardineria";
            case "Limpieza":
                return "hogar_limpieza";
            case "Mudanzas":
                return "hogar_mudanzas";
            case "Pintura":
                return "hogar_pintura";
            case "Plancha":
                return "hogar_plancha";
            case "Reformas":
            case "Carpintería":
                return "hogar_reformas";

            // Clases
            case "Baile":
                return "clases_baile";
            case "Colegio":
            case "Apoyo Escolar":
                return "clases_colegio";
            case "Dibujo":
                return "clases_dibujo";
            case "ESO":
                return "clases_eso";
            case "Idiomas":
                return "clases_idiomas";
            case "Música":
            case "Música e Instrumentos":
                return "clases_musica";

            // Cuidado
            case "Ancianos":
                return "cuidados_ancianos";
            case "Niños":
                return "cuidados_ninos";

            // Otros
            case "Fotografía":
                return "otros_foto";
            case "Informática":
            case "Reparación de PC":
            case "Reparación de Móviles":
            case "Instalación de Redes":
            case "Recuperación de Datos":
                return "otros_informatica";
            case "Redes sociales":
                return "otros_redes_sociales";
            case "Tattoo":
                return "otros_tattoo";
            case "Vídeo":
            case "Videografía":
                return "otros_video";
            case "Web":
                return "otros_web";

            default:
                return fallbackIcon;
        }
    }
}