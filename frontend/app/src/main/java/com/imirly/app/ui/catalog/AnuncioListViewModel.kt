package com.imirly.app.ui.catalog

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.imirly.app.network.AnuncioResponse
import com.imirly.app.network.RetrofitClient
import kotlinx.coroutines.launch

class AnunciosListViewModel : ViewModel() {
    var anuncios = mutableStateOf<List<AnuncioResponse>>(emptyList())
    // 👇 NUEVO: Guardamos los IDs de los anuncios favoritos del usuario 👇
    var favoritosIds = mutableStateOf<Set<String>>(emptySet())
    var isLoading = mutableStateOf(true)

    fun cargarAnuncios(subcategoriaId: String) {
        viewModelScope.launch {
            try {
                // 1. Primero cargamos los favoritos del usuario para saber qué corazones pintar
                val favResponse = RetrofitClient.catalogService.getMisFavoritos()
                if (favResponse.isSuccessful) {
                    favoritosIds.value = favResponse.body()?.map { it.id }?.toSet() ?: emptySet()
                }

                // 2. Luego cargamos los anuncios de la subcategoría
                val response = RetrofitClient.catalogService.getAnunciosBySubcategoria(subcategoriaId)
                if (response.isSuccessful) {
                    anuncios.value = response.body() ?: emptyList()
                }
            } catch (e: Exception) {
                // Error de red
            } finally {
                isLoading.value = false
            }
        }
    }

    // 👇 NUEVA FUNCIÓN: Al pulsar el corazón 👇
    fun toggleFavorito(anuncioId: String) {
        val esFavorito = favoritosIds.value.contains(anuncioId)

        // 1. Actualizamos la interfaz AL INSTANTE para que se sienta rápido (Optimistic UI)
        if (esFavorito) {
            favoritosIds.value = favoritosIds.value - anuncioId
        } else {
            favoritosIds.value = favoritosIds.value + anuncioId
        }

        // 2. Mandamos la orden al backend en segundo plano
        viewModelScope.launch {
            try {
                if (esFavorito) {
                    RetrofitClient.catalogService.removeFavorito(anuncioId)
                } else {
                    RetrofitClient.catalogService.addFavorito(anuncioId)
                }
            } catch (e: Exception) {
                // Si falla internet, revertimos el color del corazón
                if (esFavorito) favoritosIds.value = favoritosIds.value + anuncioId
                else favoritosIds.value = favoritosIds.value - anuncioId
            }
        }
    }
}