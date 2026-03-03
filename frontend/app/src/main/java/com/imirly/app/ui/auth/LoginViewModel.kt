package com.imirly.app.ui.auth

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.imirly.app.network.LoginUserRequest
import com.imirly.app.network.RetrofitClient
import com.imirly.app.network.TokenManager
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel() {
    var email = mutableStateOf("")
    var password = mutableStateOf("")
    var mensaje = mutableStateOf("")
    var isLoading = mutableStateOf(false)

    // Actualización en LoginViewModel.kt
    fun iniciarSesion(onSuccess: () -> Unit) {
        if (email.value.isEmpty() || password.value.isEmpty()) {
            mensaje.value = "Introduce tus credenciales"
            return
        }
        isLoading.value = true
        viewModelScope.launch {
            try {
                val request = LoginUserRequest(email.value, password.value)
                val response = RetrofitClient.apiService.loginUser(request)
                // ... dentro del try del iniciarSesion() en LoginViewModel.kt
                if (response.isSuccessful) {
                    // El backend nos devuelve el token como un string plano
                    val token = response.body()?.string()
                    if (token != null) {
                        TokenManager.jwtToken = token // ¡Lo guardamos!
                        mensaje.value = "¡Sesión iniciada con éxito!"
                        onSuccess()
                    } else {
                        mensaje.value = "Error: Token vacío"
                    }
                } else {
                    mensaje.value = "Email o contraseña incorrectos"
                }
            } catch (e: Exception) {
                mensaje.value = "Error de conexión con el servidor"
            } finally {
                isLoading.value = false
            }
        }
    }
}