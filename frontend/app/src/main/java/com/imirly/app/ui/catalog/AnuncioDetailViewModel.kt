package com.imirly.app.ui.catalog

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.imirly.app.network.AnuncioResponse
import com.imirly.app.network.CrearSolicitudRequest
import com.imirly.app.network.RetrofitClient
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class AnuncioDetailViewModel : ViewModel() {
    var anuncio = mutableStateOf<AnuncioResponse?>(null)
    var isLoading = mutableStateOf(true)

    var showContactDialog = mutableStateOf(false)
    var mensajeContacto = mutableStateOf("")
    var isSubmitting = mutableStateOf(false)
    var requestSuccess = mutableStateOf(false)

    var yaContactado = mutableStateOf(false)
    var esMiAnuncio = mutableStateOf(false)

    fun cargarDetalle(id: String) {
        isLoading.value = true
        viewModelScope.launch {
            try {
                // Lanzamos todas las peticiones en paralelo para ir rápido
                val anuncioDeferred = async { RetrofitClient.catalogService.getAnuncioById(id) }
                val misAnunciosDeferred = async { RetrofitClient.catalogService.getMisAnuncios() }
                val misSolicitudesDeferred = async { RetrofitClient.solicitudService.getSolicitudesCliente() }

                val anuncioRes = anuncioDeferred.await()
                if (anuncioRes.isSuccessful) {
                    anuncio.value = anuncioRes.body()

                    // Esperamos a las comprobaciones antes de quitar el cargador
                    val misAnuncios = misAnunciosDeferred.await().body() ?: emptyList()
                    esMiAnuncio.value = misAnuncios.any { it.id == id }

                    val misSolicitudes = misSolicitudesDeferred.await().body() ?: emptyList()
                    yaContactado.value = misSolicitudes.any { it.anuncioId == id }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                // Solo ahora permitimos que se vea el botón de contactar
                isLoading.value = false
            }
        }
    }

    fun enviarSolicitud(anuncioId: String) {
        // DOBLE BLOQUEO: Si ya se está enviando o ya se contactó, ignoramos cualquier clic extra
        if (isSubmitting.value || yaContactado.value || mensajeContacto.value.isBlank()) return

        isSubmitting.value = true
        yaContactado.value = true // Bloqueo visual inmediato del botón

        viewModelScope.launch {
            try {
                val request = CrearSolicitudRequest(anuncioId, mensajeContacto.value)
                val response = RetrofitClient.solicitudService.crearSolicitud(request)

                if (response.isSuccessful) {
                    requestSuccess.value = true
                    mensajeContacto.value = ""
                } else {
                    yaContactado.value = false // Si falló de verdad, permitimos reintentar
                }
            } catch (e: Exception) {
                yaContactado.value = false
                e.printStackTrace()
            } finally {
                isSubmitting.value = false
            }
        }
    }
}