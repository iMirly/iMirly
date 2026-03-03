package com.imirly.app.ui.auth

import android.util.Patterns
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.imirly.app.network.RegisterUserRequest
import com.imirly.app.network.RetrofitClient
import kotlinx.coroutines.launch

class RegisterViewModel : ViewModel() {

    // Variables de estado que la pantalla leerá (y actualizará)
    var nombre = mutableStateOf("")
    var email = mutableStateOf("")
    var password = mutableStateOf("")
    var confirmPassword = mutableStateOf("")

    // Para mostrar mensajes de éxito o error en la pantalla
    var mensaje = mutableStateOf("")
    var isLoading = mutableStateOf(false)

    fun registrarUsuario() {
        // 1. Validaciones locales (antes de llamar al servidor)
        if (nombre.value.isBlank() || email.value.isBlank() || password.value.isBlank()) {
            mensaje.value = "Todos los campos son obligatorios"
            return
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email.value).matches()) {
            mensaje.value = "Formato de e-mail inválido"
            return
        }

        if (password.value != confirmPassword.value) {
            mensaje.value = "Las contraseñas no coinciden"
            return
        }

        if (password.value.length < 6) {
            mensaje.value = "La contraseña debe tener al menos 6 caracteres"
            return
        }

        isLoading.value = true
        viewModelScope.launch {
            try {
                val request = RegisterUserRequest(
                    nombre = nombre.value,
                    email = email.value,
                    rawPassword = password.value,
                    tipoUsuario = "CLIENTE",
                    documentoIdentidad = "00000000A"
                )

                val response = RetrofitClient.apiService.registerUser(request)

                when (response.code()) {
                    201 -> mensaje.value = "¡Cuenta creada con éxito!"
                    409 -> mensaje.value = "Este e-mail ya está registrado"
                    400 -> mensaje.value = "Datos inválidos. Revisa el formulario"
                    else -> mensaje.value = "Error del servidor: ${response.code()}"
                }
            } catch (e: Exception) {
                mensaje.value = "Error de red: Comprueba tu conexión"
            } finally {
                isLoading.value = false
            }
        }
    }
}