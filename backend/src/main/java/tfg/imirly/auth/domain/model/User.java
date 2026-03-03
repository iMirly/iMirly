package tfg.imirly.auth.domain.model;

import java.util.UUID;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
public class User {
    private UUID id;
    private String nombre;
    private String email;
    private String passwordHash;
    private UserType tipo;
    private String documentoIdentidad;
    private boolean verificado;
    private LocalDateTime fechaRegistro;

    private Double saldo; // <--- NUEVO: Monedero virtual
    private Double puntuacionTotal;
    private Integer numeroValoraciones;

    public User(String nombre, String email, String passwordHash, UserType tipo, String documentoIdentidad) {
        this.nombre = nombre;
        this.email = email;
        this.passwordHash = passwordHash;
        this.tipo = tipo;
        this.documentoIdentidad = documentoIdentidad;
        this.verificado = false;
        this.fechaRegistro = LocalDateTime.now();
        this.saldo = 0.0; // <--- NUEVO: Por defecto empiezan a cero
        this.puntuacionTotal = 0.0;
        this.numeroValoraciones = 0;
    }

    public User() {
    }

    public void updateProfile(String nuevoNombre, String nuevoDocumento, String nuevoEmail) {
        if (nuevoNombre != null && !nuevoNombre.isBlank()) {
            this.nombre = nuevoNombre;
        }
        if (nuevoDocumento != null && !nuevoDocumento.isBlank()) {
            this.documentoIdentidad = nuevoDocumento;
        }
        if (nuevoEmail != null && !nuevoEmail.isBlank()) {
            this.email = nuevoEmail;
        }
    }

    public void changePassword(String newPasswordHash) {
        this.passwordHash = newPasswordHash;
    }

    public void restarSaldo(Double cantidad) {
        if (cantidad == null || cantidad <= 0) {
            throw new IllegalArgumentException("La cantidad a restar debe ser mayor que cero.");
        }
        if (this.saldo == null)
            this.saldo = 0.0;

        this.saldo -= cantidad;
    }

    public void sumarSaldo(Double cantidad) {
        if (cantidad == null || cantidad <= 0) {
            throw new IllegalArgumentException("La cantidad a sumar debe ser mayor que cero.");
        }
        if (this.saldo == null)
            this.saldo = 0.0;
        this.saldo += cantidad;
    }

    public void addValoracion(Double nota) {
        if (nota == null || nota < 0 || nota > 5)
            throw new IllegalArgumentException("Nota inválida");
        if (this.puntuacionTotal == null)
            this.puntuacionTotal = 0.0;
        if (this.numeroValoraciones == null)
            this.numeroValoraciones = 0;
        this.puntuacionTotal += nota;
        this.numeroValoraciones++;
    }

    public Double getValoracionMedia() {
        if (numeroValoraciones == null || numeroValoraciones == 0)
            return 0.0;
        return Math.round((puntuacionTotal / numeroValoraciones) * 10.0) / 10.0;
    }
}