package com.imirly.app.network

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import okhttp3.ResponseBody
data class RegisterUserRequest(
    val nombre: String,
    val email: String,
    val rawPassword: String,
    val tipoUsuario: String,
    val documentoIdentidad: String
)

data class LoginUserRequest(
    val email: String,
    val rawPassword: String
)

interface AuthApiService {
    @POST("/api/v1/auth/register")
    suspend fun registerUser(@Body request: RegisterUserRequest): Response<ResponseBody> // Cambia String por ResponseBody

    @POST("/api/v1/auth/login")
    suspend fun loginUser(@Body request: LoginUserRequest): Response<ResponseBody>
}