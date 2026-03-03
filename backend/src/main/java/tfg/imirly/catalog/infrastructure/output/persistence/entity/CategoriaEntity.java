package tfg.imirly.catalog.infrastructure.output.persistence.entity;

import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "categoria")
public class CategoriaEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, unique = true, length = 100)
    private String nombre;

    @Column(length = 50)
    private String icono;

    // Getters y Setters vacíos (Si usas Lombok, puedes borrar esto y poner @Data arriba)
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getIcono() { return icono; }
    public void setIcono(String icono) { this.icono = icono; }
}