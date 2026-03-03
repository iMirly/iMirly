package com.imirly.app.ui.main

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.imirly.app.network.CategoriaResponse
import com.imirly.app.network.RetrofitClient
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {
    var categorias = mutableStateOf<List<CategoriaResponse>>(emptyList())
    var isLoading = mutableStateOf(true)

    init {
        cargarCategorias()
    }

    private fun cargarCategorias() {
        viewModelScope.launch {
            try {
                val response = RetrofitClient.catalogService.getCategorias()
                if (response.isSuccessful) {
                    categorias.value = response.body() ?: emptyList()
                }
            } catch (e: Exception) {
                // Manejar el error de conexión si es necesario
            } finally {
                isLoading.value = false
            }
        }
    }
}