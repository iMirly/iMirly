package com.imirly.app.ui.catalog

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.imirly.app.network.AnuncioResponse
import com.imirly.app.network.CrearSolicitudRequest
import com.imirly.app.network.RetrofitClient
import kotlinx.coroutines.launch

class AnuncioDetailViewModel : ViewModel() {
    var anuncio = mutableStateOf<AnuncioResponse?>(null)
    var isLoading = mutableStateOf(true)

    // Nuevas variables para el flujo de contratación
    var showContactDialog = mutableStateOf(false)
    var mensajeContacto = mutableStateOf("")
    var isSubmitting = mutableStateOf(false)
    var requestSuccess = mutableStateOf(false)

    fun cargarDetalle(id: String) {
        isLoading.value = true
        viewModelScope.launch {
            try {
                val response = RetrofitClient.catalogService.getAnuncioById(id)
                if (response.isSuccessful) anuncio.value = response.body()
            } finally { isLoading.value = false }
        }
    }

    // NUEVA FUNCIÓN: Enviar la solicitud al backend
    // NUEVA FUNCIÓN: Enviar la solicitud al backend
    fun enviarSolicitud(anuncioId: String) {
        if (mensajeContacto.value.isBlank()) return // No enviamos vacío

        isSubmitting.value = true
        viewModelScope.launch {
            try {
                val request = CrearSolicitudRequest(anuncioId, mensajeContacto.value)
                val response = RetrofitClient.solicitudService.crearSolicitud(request)

                if (response.isSuccessful) {
                    requestSuccess.value = true
                    // El showContactDialog.value = false ya lo pusimos en la vista, así que perfecto
                } else {
                    println("Error del servidor: ${response.code()}")
                }
            } catch (e: Exception) {
                // 👇 AÑADE ESTO para no volver a tener bugs silenciosos 👇
                println("Fallo crítico en Retrofit: ${e.message}")
                e.printStackTrace()
            } finally {
                isSubmitting.value = false
            }
        }
    }
}