package com.imirly.app.network

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path

// --- DTOs (Modelos de datos) ---
data class CrearSolicitudRequest(
    val anuncioId: String,
    val detalles: String
)

data class SolicitudResponse(
    val id: String,
    val clienteId: String,
    val anuncioId: String,
    val estado: String,
    val detallesSolicitud: String,
    val fechaCreacion: String,
    val nombreCliente: String? = "Cliente",
    val proveedorId: String? = null,
    val nombreProveedor: String? = "Profesional",
    var soyElProveedor: Boolean = false
)

data class CambiarEstadoRequest(
    val nuevoEstado: String
)

// --- INTERFAZ API ---
interface SolicitudApiService {
    // 1. Crear solicitud
    // 1. Crear solicitud
    // 1. Crear solicitud
    @POST("/api/v1/solicitudes")
    suspend fun crearSolicitud(@Body request: CrearSolicitudRequest): Response<SolicitudResponse>

    // 2. Ver la bandeja de entrada del profesional
    @GET("/api/v1/solicitudes/proveedor")
    suspend fun getSolicitudesProveedor(): Response<List<SolicitudResponse>>

    @GET("/api/v1/solicitudes/cliente")
    suspend fun getSolicitudesCliente(): Response<List<SolicitudResponse>>

    // 3. Aceptar o Rechazar la solicitud
    @PATCH("/api/v1/solicitudes/{id}/estado")
    suspend fun cambiarEstadoSolicitud(
        @Path("id") id: String,
        @Body request: CambiarEstadoRequest
    ): Response<SolicitudResponse>

    // 4. Borrar todos los chats (solicitudes) entre dos usuarios
    @retrofit2.http.DELETE("/api/v1/solicitudes/conversacion/{otroUsuarioId}")
    suspend fun borrarSolicitudesPorUsuarios(@Path("otroUsuarioId") otroUsuarioId: String): Response<Unit>

    // 4. Eliminar la solicitud para borrar el chat de la bandeja
    @DELETE("/api/v1/solicitudes/{id}")
    suspend fun eliminarSolicitud(@Path("id") id: String): Response<Unit>

    // 5. Pagar el presupuesto (Retener dinero en el Escrow/Limbo)
    @POST("/api/v1/solicitudes/{id}/pagar")
    suspend fun pagarSolicitud(@Path("id") id: String): Response<SolicitudResponse>
}