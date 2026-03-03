package com.imirly.app.ui.publish

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.imirly.app.network.CategoriaResponse
import com.imirly.app.network.CreateAnuncioRequest
import com.imirly.app.network.RetrofitClient
import com.imirly.app.network.SubcategoriaResponse
import kotlinx.coroutines.launch

class PublishViewModel : ViewModel() {
    // Control de flujo (Paso 1 o 2)
    var currentStep = mutableStateOf(1)

    // Datos del anuncio (Persistencia en base de datos)
    var titulo = mutableStateOf("")
    var precio = mutableStateOf("")
    var ubicacion = mutableStateOf("")
    var descripcion = mutableStateOf("")
    var subcategoriaId = mutableStateOf("")
    var subcategoriaNombre = mutableStateOf("Selecciona una subcategoría")

    // Listas para los selectores del Paso 2
    var categorias = mutableStateOf<List<CategoriaResponse>>(emptyList())
    var subcategorias = mutableStateOf<List<SubcategoriaResponse>>(emptyList())
    var categoriaSeleccionadaId = mutableStateOf("")

    var isLoading = mutableStateOf(false)
    var errorMensaje = mutableStateOf("")

    init {
        cargarCategorias()
    }

    // Carga inicial de categorías principales
    private fun cargarCategorias() {
        viewModelScope.launch {
            try {
                val response = RetrofitClient.catalogService.getCategorias()
                if (response.isSuccessful) {
                    categorias.value = response.body() ?: emptyList()
                }
            } catch (e: Exception) {
                errorMensaje.value = "Error al cargar categorías"
            }
        }
    }

    // Carga subcategorías al seleccionar una categoría padre
    fun cargarSubcategorias(catId: String) {
        categoriaSeleccionadaId.value = catId
        subcategoriaId.value = "" // Reset subcat al cambiar de categoría
        viewModelScope.launch {
            try {
                val response = RetrofitClient.catalogService.getSubcategorias(catId)
                if (response.isSuccessful) {
                    subcategorias.value = response.body() ?: emptyList()
                }
            } catch (e: Exception) {
                errorMensaje.value = "Error al cargar subcategorías"
            }
        }
    }

    fun publicarAnuncio(onSuccess: () -> Unit) {
        if (subcategoriaId.value.isEmpty()) {
            errorMensaje.value = "Debes seleccionar una subcategoría"
            return
        }

        isLoading.value = true
        viewModelScope.launch {
            try {
                val request = CreateAnuncioRequest(
                    subcategoriaId = subcategoriaId.value,
                    titulo = titulo.value,
                    descripcion = descripcion.value,
                    precioHora = precio.value.toDoubleOrNull() ?: 0.0,
                    ubicacion = ubicacion.value
                )
                val response = RetrofitClient.catalogService.crearAnuncio(request)
                if (response.isSuccessful) {
                    onSuccess()
                    resetForm()
                } else {
                    errorMensaje.value = "Error del servidor: ${response.code()}"
                }
            } catch (e: Exception) {
                errorMensaje.value = "Error de conexión"
            } finally {
                isLoading.value = false
            }
        }
    }

    private fun resetForm() {
        titulo.value = ""
        precio.value = ""
        ubicacion.value = ""
        descripcion.value = ""
        subcategoriaId.value = ""
        categoriaSeleccionadaId.value = ""
        subcategorias.value = emptyList()
        currentStep.value = 1
        errorMensaje.value = ""
    }
}