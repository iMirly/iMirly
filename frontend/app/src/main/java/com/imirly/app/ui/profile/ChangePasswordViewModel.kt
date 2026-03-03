package com.imirly.app.ui.profile

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.imirly.app.network.ChangePasswordRequest
import com.imirly.app.network.RetrofitClient
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class ChangePasswordViewModel : ViewModel() {
    var oldPassword = mutableStateOf("")
    var newPassword = mutableStateOf("")
    var confirmPassword = mutableStateOf("")

    var isLoading = mutableStateOf(false)
    var mensaje = mutableStateOf("")

    fun cambiarContrasena(onSuccess: () -> Unit) {
        // 1. Validaciones de la interfaz
        if (oldPassword.value.isBlank() || newPassword.value.isBlank() || confirmPassword.value.isBlank()) {
            mensaje.value = "Por favor, rellena todos los campos."
            return
        }
        if (newPassword.value != confirmPassword.value) {
            mensaje.value = "Las contraseñas nuevas no coinciden."
            return
        }
        if (newPassword.value.length < 6) {
            mensaje.value = "La nueva contraseña debe tener al menos 6 caracteres."
            return
        }

        // 2. Llamada al Backend
        isLoading.value = true
        mensaje.value = ""

        viewModelScope.launch {
            try {
                val request = ChangePasswordRequest(oldPassword.value, newPassword.value)
                val response = RetrofitClient.userService.changePassword(request)

                if (response.isSuccessful) {
                    mensaje.value = "¡Contraseña actualizada con éxito!"
                    delay(1500) // Esperamos un segundo y medio para que el usuario lea el mensaje
                    onSuccess() // Ejecutamos la acción de volver atrás
                } else {
                    mensaje.value = "La contraseña actual es incorrecta."
                }
            } catch (e: Exception) {
                mensaje.value = "Error de conexión con el servidor."
            } finally {
                isLoading.value = false
            }
        }
    }
}