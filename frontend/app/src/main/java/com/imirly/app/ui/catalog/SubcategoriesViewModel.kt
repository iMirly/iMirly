package com.imirly.app.ui.catalog

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.imirly.app.network.RetrofitClient
import com.imirly.app.network.SubcategoriaResponse
import kotlinx.coroutines.launch

// 1. EL CEREBRO (ViewModel): Se encarga de llamar al backend
class SubcategoriesViewModel : ViewModel() {
    var subcategorias = mutableStateOf<List<SubcategoriaResponse>>(emptyList())
    var isLoading = mutableStateOf(true)

    fun cargarSubcategorias(categoriaId: String) {
        viewModelScope.launch {
            try {
                // Llamamos al endpoint que configuramos en el backend
                val response = RetrofitClient.catalogService.getSubcategorias(categoriaId)
                if (response.isSuccessful) {
                    subcategorias.value = response.body() ?: emptyList()
                }
            } catch (e: Exception) {
                // Error de conexión
            } finally {
                isLoading.value = false
            }
        }
    }
}