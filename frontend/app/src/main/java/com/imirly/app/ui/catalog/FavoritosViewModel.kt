package com.imirly.app.ui.catalog

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.imirly.app.network.AnuncioResponse
import com.imirly.app.network.RetrofitClient
import kotlinx.coroutines.launch

class FavoritosViewModel : ViewModel() {
    var favoritos = mutableStateOf<List<AnuncioResponse>>(emptyList())
    var isLoading = mutableStateOf(true)

    fun cargarFavoritos() {
        isLoading.value = true
        viewModelScope.launch {
            try {
                val response = RetrofitClient.catalogService.getMisFavoritos()
                if (response.isSuccessful) {
                    favoritos.value = response.body() ?: emptyList()
                }
            } catch (e: Exception) {
                // Manejo de errores
            } finally {
                isLoading.value = false
            }
        }
    }

    fun quitarFavorito(anuncioId: String) {
        viewModelScope.launch {
            try {
                val response = RetrofitClient.catalogService.removeFavorito(anuncioId)
                if (response.isSuccessful) {
                    // Lo borramos de la lista de la pantalla instantáneamente
                    favoritos.value = favoritos.value.filter { it.id != anuncioId }
                }
            } catch (e: Exception) { }
        }
    }
}