package tfg.imirly.config;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import jakarta.persistence.EntityManager;

import tfg.imirly.catalog.infrastructure.output.persistence.entity.CategoriaEntity;
import tfg.imirly.catalog.infrastructure.output.persistence.entity.SubcategoriaEntity;

// imports removed

@Component
public class DatabaseSeeder implements ApplicationListener<ApplicationReadyEvent> {

    private final JdbcTemplate jdbcTemplate;
    private final EntityManager entityManager;

    public DatabaseSeeder(JdbcTemplate jdbcTemplate, EntityManager entityManager) {
        this.jdbcTemplate = jdbcTemplate;
        this.entityManager = entityManager;
    }

    @Override
    @Transactional
    public void onApplicationEvent(ApplicationReadyEvent event) {

        // Comprobamos si la base de datos ya está poblada
        Integer count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM categoria", Integer.class);
        if (count != null && count > 0) {
            System.out.println("✅ La base de datos ya tiene categorías. Saltando Seed.");
            return;
        }

        try {
            System.out.println("=========================================================");
            System.out.println("🚀 EJECUTANDO SEEDER: Poblando BD vacía... 🚀");
            System.out.println("=========================================================");

            // Vaciar toda la base de datos por seguridad (si hay residual)
            jdbcTemplate.execute("TRUNCATE TABLE usuarios, categoria CASCADE");

            // Categorías y subcategorías alineadas a los archivos .jpg de la carpeta res/drawable
            
            CategoriaEntity hogar = crearCategoria("Hogar", "hogar");
            crearSubcategoria("Electricista", "hogar_electricista", hogar);
            crearSubcategoria("Electrodomésticos", "hogar_electrodomesticos", hogar);
            crearSubcategoria("Fontanería", "hogar_fontaneria", hogar);
            crearSubcategoria("Jardinería", "hogar_jardineria", hogar);
            crearSubcategoria("Limpieza", "hogar_limpieza", hogar);
            crearSubcategoria("Mudanzas", "hogar_mudanzas", hogar);
            crearSubcategoria("Pintura", "hogar_pintura", hogar);
            crearSubcategoria("Plancha", "hogar_plancha", hogar);
            crearSubcategoria("Reformas", "hogar_reformas", hogar);

            CategoriaEntity belleza = crearCategoria("Estética y Belleza", "belleza");
            crearSubcategoria("Depilación", "belleza_depilacion", belleza);
            crearSubcategoria("Facial", "belleza_facial", belleza);
            crearSubcategoria("Maquillaje", "belleza_maquillaje", belleza);
            crearSubcategoria("Masajes", "belleza_masajes", belleza);
            crearSubcategoria("Peluquería", "belleza_peluqueria", belleza);
            crearSubcategoria("Uñas", "belleza_unas", belleza);
            
            CategoriaEntity clases = crearCategoria("Educación y Clases", "clases");
            crearSubcategoria("Baile", "clases_baile", clases);crearSubcategoria("Colegio", "clases_colegio", clases);
            crearSubcategoria("Dibujo", "clases_dibujo", clases);
            crearSubcategoria("ESO", "clases_eso", clases);
            
            crearSubcategoria("Idiomas", "clases_idiomas", clases);
            crearSubcategoria("Música", "clases_musica", clases);
            CategoriaEntity cuidado = crearCategoria("Cuidado", "cuidado");
            crearSubcategoria("Ancianos", "cuidados_ancianos", cuidado);
            crearSubcategoria("Niños", "cuidados_ninos", cuidado);

            CategoriaEntity deporte = crearCategoria("Deporte y Salud", "deporte");
            crearSubcategoria("Boxeo", "deporte_boxeo", deporte);
            crearSubcategoria("Entrenador Personal", "deporte_entrenador_personal", deporte);
            crearSubcategoria("Pádel", "deporte_padel", deporte);
            crearSubcategoria("Pilates", "deporte_pilates", deporte);
            crearSubcategoria("Tenis", "deporte_tenis", deporte);
            crearSubcategoria("Yoga", "deporte_yoga", deporte);

            CategoriaEntity mascotas = crearCategoria("Mascotas", "mascotas");
            crearSubcategoria("Adiestrador", "mascotas_adiestrador", mascotas);
            crearSubcategoria("Conducta", "mascotas_conducta", mascotas);
            crearSubcategoria("Cuidador a Domicilio", "mascotas_cuidador", mascotas);
            crearSubcategoria("Paseador", "mascotas_paseador", mascotas);
            crearSubcategoria("Peluquería Canina", "mascotas_peluqueria", mascotas);

            CategoriaEntity otros = crearCategoria("Eventos y Otros", "otros");
            crearSubcategoria("Fotografía", "otros_foto", otros);
            crearSubcategoria("Informática", "otros_informatica", otros);
            crearSubcategoria("Redes Sociales", "otros_redes_sociales", otros);
            crearSubcategoria("Tattoo", "otros_tattoo", otros);
            crearSubcategoria("Vídeo", "otros_video", otros);
            crearSubcategoria("Web", "otros_web", otros);

            System.out.println("✅ BASE DE DATOS INICIALIZADA EXITOSAMENTE! ✅");
        } catch (Exception e) {
            System.err.println("Ocurrió un error inicializando los datos: " + e.getMessage());
        }
    }

    private CategoriaEntity crearCategoria(String nombreCat, String iconoCat) {
        CategoriaEntity categoria = new CategoriaEntity();
        categoria.setNombre(nombreCat);
        categoria.setIcono(iconoCat);
        entityManager.persist(categoria);
        return categoria;
    }

    private void crearSubcategoria(String nombreSub, String iconoSub, CategoriaEntity categoria) {
        SubcategoriaEntity sub = new SubcategoriaEntity();
        sub.setNombre(nombreSub);
        sub.setCategoria(categoria);
        sub.setIcono(iconoSub);
        entityManager.persist(sub);
    }
}
