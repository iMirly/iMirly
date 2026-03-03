package tfg.imirly.contracting.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SolicitudContrato {
    private UUID id;
    private UUID clienteId;
    private UUID proveedorId;
    private UUID anuncioId;
    private EstadoSolicitud estado;
    private String detallesSolicitud;
    private Double precioAcordado;
    private LocalDateTime fechaCreacion;
    private String nombreProveedor;
    private String nombreCliente;

    // Constructor para nuevas solicitudes (Estado inicial PENDIENTE)
    public SolicitudContrato(UUID clienteId, UUID anuncioId, String detallesSolicitud) {
        this.clienteId = clienteId;
        this.anuncioId = anuncioId;
        this.detallesSolicitud = detallesSolicitud;
        this.estado = EstadoSolicitud.PENDIENTE;
        this.fechaCreacion = LocalDateTime.now();
    }

    // Lógica de negocio: solo se puede aceptar si está pendiente
    public void aceptar() {
        if (this.estado == EstadoSolicitud.PENDIENTE) {
            this.estado = EstadoSolicitud.ACEPTADO;
        } else {
            throw new IllegalStateException("Solo se pueden aceptar solicitudes pendientes.");
        }
    }

    public void completar() {
        if (this.estado == EstadoSolicitud.EN_CURSO) {
            this.estado = EstadoSolicitud.COMPLETADO;
        } else {
            throw new IllegalStateException("Solo se puede completar un servicio que está en curso.");
        }
    }

    // Lógica para el paso: "Proveedor cierra precio"
    public void proponerPrecio(Double precio) {
        this.precioAcordado = precio;
        this.estado = EstadoSolicitud.PENDIENTE_PAGO;
    }

    // Lógica para el paso: "Cliente paga"
    public void confirmarPago() {
        if (this.estado == EstadoSolicitud.PENDIENTE_PAGO) {
            this.estado = EstadoSolicitud.PAGADO;
        }
    }

    // Lógica para el paso: "Proveedor confirma trabajo hecho"
    public void marcarComoTerminado() {
        this.estado = EstadoSolicitud.REVISION_PENDIENTE;
    }

    // Lógica para el paso crítico: "Cliente confirma si es bueno (SÍ/NO)"
    public void validarTrabajo(boolean esBueno) {
        if (esBueno) {
            this.estado = EstadoSolicitud.COMPLETADO; // El proveedor recibe el pago
        } else {
            this.estado = EstadoSolicitud.PAGADO; // Vuelve a estar "en curso" para arreglarlo
        }
    }
    // LÓGICA DE NEGOCIO: "El cliente aprueba el presupuesto"
    public void pagarPresupuesto() {
        if (this.estado != EstadoSolicitud.PENDIENTE_PAGO) {
            throw new IllegalStateException("Solo se puede pagar una solicitud pendiente de pago.");
        }
        // El dinero queda retenido y el estado avanza
        this.estado = EstadoSolicitud.PAGADO;
    }
}