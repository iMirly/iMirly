package com.imirly.app.ui.messages

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.imirly.app.network.CambiarEstadoRequest
import com.imirly.app.network.RetrofitClient
import com.imirly.app.network.SolicitudResponse
import kotlinx.coroutines.launch

class MessagesViewModel : ViewModel() {
    var solicitudes = mutableStateOf<List<SolicitudResponse>>(emptyList())
    var isLoading = mutableStateOf(true)

    init {
        cargarSolicitudes()
    }

    fun cargarSolicitudes() {
        viewModelScope.launch {
            try {
                isLoading.value = true
                val responseProv = RetrofitClient.solicitudService.getSolicitudesProveedor()
                val responseCli = RetrofitClient.solicitudService.getSolicitudesCliente()

                // Marcamos quiénes somos en cada lista
                val listaProv = responseProv.body()?.map { it.apply { soyElProveedor = true } } ?: emptyList()
                val listaCli = responseCli.body()?.map { it.apply { soyElProveedor = false } } ?: emptyList()

                solicitudes.value = (listaProv + listaCli).distinctBy { it.id }
            } catch (e: Exception) { /* ... */ } finally { isLoading.value = false }
        }
    }

    fun cambiarEstado(solicitudId: String, nuevoEstado: String) {
        viewModelScope.launch {
            try {
                val request = CambiarEstadoRequest(nuevoEstado)
                val response = RetrofitClient.solicitudService.cambiarEstadoSolicitud(solicitudId, request)
                if (response.isSuccessful) {
                    // Si el backend lo cambia bien, recargamos la lista para ver el cambio visual
                    cargarSolicitudes()
                }
            } catch (e: Exception) {
                // Manejar error
            }
        }
    }
}