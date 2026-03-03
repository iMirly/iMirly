package com.imirly.app.network

import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Path

// Lo que enviamos para editar
data class EditProfileRequest(
    val nombre: String,
    val documentoIdentidad: String,
    val email: String // NUEVO
)

// NUEVO: Lo que recibimos al consultar
data class UserProfileResponse(
    val id: String,
    val nombre: String,
    val email: String,
    val documentoIdentidad: String?,
    val saldo: Double = 0.0,
    val valoracionMedia: Double = 0.0,
    val numeroValoraciones: Int = 0
)

data class TransaccionDTO(
    val titulo: String,
    val subTitulo: String,
    val cantidadFormateada: String,
    val estado: String,
    val tiempo: String
)

data class SaldoResponse(
    val disponible: Double,
    val pendiente: Double,
    val esteMes: Double,
    val recientes: List<TransaccionDTO>
)

data class ChangePasswordRequest(
    val oldPassword: String,
    val newPassword: String
)

interface UserApiService {
    @GET("/api/v1/users/profile/{email}")
    suspend fun getUserProfile(@Path("email") email: String): Response<UserProfileResponse>

    @PUT("/api/v1/users/profile/{email}")
    suspend fun editProfile(
        @Path("email") email: String,
        @Body request: EditProfileRequest
    ): Response<okhttp3.ResponseBody>

    @PUT("/api/v1/users/password")
    suspend fun changePassword(@Body request: ChangePasswordRequest): Response<okhttp3.ResponseBody>

    @retrofit2.http.POST("/api/v1/users/saldo/ingresar")
    suspend fun ingresarSaldo(): Response<okhttp3.ResponseBody>

    @retrofit2.http.POST("/api/v1/users/saldo/retirar")
    suspend fun retirarSaldo(): Response<okhttp3.ResponseBody>

    @GET("/api/v1/users/wallet")
    suspend fun getWallet(): Response<SaldoResponse>
}