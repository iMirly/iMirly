package com.imirly.app.ui.profile

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.imirly.app.network.EditProfileRequest
import com.imirly.app.network.RetrofitClient
import com.imirly.app.network.TokenManager
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class ProfileViewModel : ViewModel() {

    // Datos reales del usuario (para mostrar en la pantalla)
    var userName = mutableStateOf("Cargando...")
    var userDni = mutableStateOf("")
    var userEmail = mutableStateOf("")
    var valoracionMedia = mutableStateOf(0.0)
    var numeroValoraciones = mutableStateOf(0)

    var editEmail = mutableStateOf("")

    // Variables del diálogo
    var showEditDialog = mutableStateOf(false)
    var editNombre = mutableStateOf("")
    var editDni = mutableStateOf("")

    var isLoading = mutableStateOf(false)
    var mensaje = mutableStateOf("")

    // Variable para controlar el aviso de cierre de sesión
    var showLogoutDialog = mutableStateOf(false)

    init {
        cargarDatosUsuario()
    }

    private fun cargarDatosUsuario() {
        val userId = TokenManager.getUserId()
        if (userId == null) {
            userName.value = "Error: Inicia sesión de nuevo"
            return
        }

        viewModelScope.launch {
            try {
                val response = RetrofitClient.userService.getUserProfile(userId)
                if (response.isSuccessful) {
                    val profile = response.body()
                    if (profile != null) {
                        userName.value = profile.nombre
                        userDni.value = profile.documentoIdentidad ?: ""
                        // 👇 SOLUCIÓN 1: GUARDAMOS EL EMAIL PARA PODER PRE-RELLENARLO 👇
                        userEmail.value = profile.email
                        valoracionMedia.value = profile.valoracionMedia
                        numeroValoraciones.value = profile.numeroValoraciones
                    }
                } else {
                    userName.value = "Error backend: ${response.code()}"
                }
            } catch (e: Exception) {
                userName.value = "Error Red: ${e.localizedMessage}"
            }
        }
    }

    // Se llama cuando el usuario pulsa "Datos personales"
    fun prepararEdicion() {
        editNombre.value = userName.value
        editDni.value = userDni.value
        // 👇 AHORA SÍ TENDRÁ DATOS 👇
        editEmail.value = userEmail.value
        showEditDialog.value = true
        mensaje.value = ""
    }

    fun actualizarPerfil(onEmailChanged: () -> Unit) {
        // 👇 SOLUCIÓN 2: VALIDACIÓN DE CAMPOS VACÍOS EN EL VIEWMODEL 👇
        if (editNombre.value.isBlank() || editEmail.value.isBlank()) {
            mensaje.value = "El nombre y el correo no pueden estar vacíos."
            return
        }

        val currentEmail = TokenManager.getUserId() ?: return
        isLoading.value = true
        mensaje.value = ""

        viewModelScope.launch {
            try {
                val request = EditProfileRequest(editNombre.value, editDni.value, editEmail.value)
                val response = RetrofitClient.userService.editProfile(currentEmail, request)

                if (response.isSuccessful) {
                    mensaje.value = "¡Perfil actualizado!"
                    userName.value = editNombre.value
                    userDni.value = editDni.value

                    val emailHaCambiado = editEmail.value != userEmail.value
                    userEmail.value = editEmail.value

                    delay(1000)
                    showEditDialog.value = false

                    if (emailHaCambiado) {
                        cerrarSesion { onEmailChanged() }
                    }
                } else {
                    mensaje.value = "Error: El correo ya existe o falló la conexión."
                }
            } catch (e: Exception) {
                mensaje.value = "Error de red."
            } finally {
                isLoading.value = false
            }
        }
    }

    fun cerrarSesion(onLogoutComplete: () -> Unit) {
        TokenManager.jwtToken = null
        onLogoutComplete()
    }
}