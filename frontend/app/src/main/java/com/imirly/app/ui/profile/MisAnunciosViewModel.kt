package com.imirly.app.ui.profile

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.imirly.app.network.AnuncioResponse
import com.imirly.app.network.RetrofitClient
import com.imirly.app.network.UpdateAnuncioRequest
import kotlinx.coroutines.launch

class MisAnunciosViewModel : ViewModel() {
    var anunciosActivos = mutableStateOf<List<AnuncioResponse>>(emptyList())
    var anunciosInactivos = mutableStateOf<List<AnuncioResponse>>(emptyList())
    var isLoading = mutableStateOf(true)
    var selectedTab = mutableStateOf(0) // 0 para Activos, 1 para Inactivos

    var showDeleteConfirm = mutableStateOf(false)
    var anuncioParaBorrar = mutableStateOf<AnuncioResponse?>(null)

    // Estados para editar
    var showEditDialog = mutableStateOf(false)
    var anuncioParaEditar = mutableStateOf<AnuncioResponse?>(null)
    var editTitulo = mutableStateOf("")
    var editDescripcion = mutableStateOf("")
    var editPrecio = mutableStateOf("")
    var editUbicacion = mutableStateOf("")
    var editActivo = mutableStateOf(true)

    init {
        cargarMisAnuncios()
    }

    fun cargarMisAnuncios() {
        isLoading.value = true
        viewModelScope.launch {
            try {
                val response = RetrofitClient.catalogService.getMisAnuncios()
                if (response.isSuccessful) {
                    val todos = response.body() ?: emptyList()
                    // Filtramos por el estado 'activo'
                    anunciosActivos.value = todos.filter { it.activo }
                    anunciosInactivos.value = todos.filter { !it.activo }
                }
            } catch (e: Exception) {
                // Manejar error
            } finally {
                isLoading.value = false
            }
        }
    }

    // --- ELIMINAR ---
    fun confirmarBorrado(anuncio: AnuncioResponse) {
        anuncioParaBorrar.value = anuncio
        showDeleteConfirm.value = true
    }

    fun eliminarAnuncio() {
        val id = anuncioParaBorrar.value?.id ?: return
        viewModelScope.launch {
            try {
                val response = RetrofitClient.catalogService.deleteAnuncio(id)
                if (response.isSuccessful) {
                    cargarMisAnuncios() // Recargamos la lista
                    showDeleteConfirm.value = false
                }
            } catch (e: Exception) { /* Log error */ }
        }
    }

    // --- EDITAR ---
    fun prepararEdicion(anuncio: AnuncioResponse) {
        anuncioParaEditar.value = anuncio
        editTitulo.value = anuncio.titulo
        editDescripcion.value = anuncio.descripcion
        editPrecio.value = anuncio.precioHora.toString()
        editUbicacion.value = anuncio.ubicacion
        editActivo.value = anuncio.activo // <--- Cargar estado actual
        showEditDialog.value = true
    }

    fun actualizarAnuncio() {
        val id = anuncioParaEditar.value?.id ?: return
        viewModelScope.launch {
            try {
                val request = UpdateAnuncioRequest(
                    titulo = editTitulo.value,
                    descripcion = editDescripcion.value,
                    precioHora = editPrecio.value.toDoubleOrNull() ?: 0.0,
                    ubicacion = editUbicacion.value,
                    activo = editActivo.value // <--- Enviar nuevo estado
                )
                val response = RetrofitClient.catalogService.updateAnuncio(id, request)
                if (response.isSuccessful) {
                    cargarMisAnuncios()
                    showEditDialog.value = false
                }
            } catch (e: Exception) { /* Log error */ }
        }
    }
}