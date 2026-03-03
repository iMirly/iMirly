package com.imirly.app.network

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

// --- DTOs ---
data class EnviarMensajeRequest(
    val receptorId: String,
    val contenido: String,
    val solicitudId: String? = null,
    val tipo: String? = null
)

data class MensajeResponse(
    val id: String,
    val remitenteId: String,
    val receptorId: String,
    val contenido: String,
    val tipo: String?, // <--- NUEVO: Para saber si es PROPUESTA_PRECIO
    val timestamp: String,
    val leido: Boolean
)

// --- INTERFAZ API ---
interface MensajeApiService {
    @POST("/api/v1/mensajes")
    suspend fun enviarMensaje(@Body request: EnviarMensajeRequest): Response<MensajeResponse>

    @GET("/api/v1/mensajes/conversacion/{otroUsuarioId}")
    suspend fun obtenerConversacion(@Path("otroUsuarioId") otroUsuarioId: String): Response<List<MensajeResponse>>

    @retrofit2.http.DELETE("/api/v1/mensajes/conversacion/{otroUsuarioId}")
    suspend fun borrarConversacion(@Path("otroUsuarioId") otroUsuarioId: String): Response<Unit>
}