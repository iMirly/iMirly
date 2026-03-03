package com.imirly.app.ui.catalog

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.imirly.app.network.AnuncioResponse
import com.imirly.app.network.RetrofitClient
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SearchViewModel : ViewModel() {
    var query = mutableStateOf("")
    var ubicacion = mutableStateOf("") // Lo prepararemos para el paso de Filtros
    var resultados = mutableStateOf<List<AnuncioResponse>>(emptyList())
    var isLoading = mutableStateOf(false)
    var hasSearched = mutableStateOf(false) // Para saber si mostramos "No hay resultados"

    var categoriaId: String? = null
    private var searchJob: Job? = null

    fun onQueryChange(newQuery: String) {
        query.value = newQuery

        // Cancelamos la búsqueda anterior si el usuario sigue escribiendo
        searchJob?.cancel()

        // Esperamos 500ms antes de llamar al backend (Efecto Debounce)
        searchJob = viewModelScope.launch {
            delay(500)
            buscar()
        }
    }

    fun buscar() {
        isLoading.value = true
        hasSearched.value = true


        viewModelScope.launch {
            try {
                // Si el texto está vacío, enviamos null para que la API nos devuelva todo
                val response = RetrofitClient.catalogService.searchAnuncios(
                    query = query.value.ifBlank { null },
                    ubicacion = ubicacion.value.ifBlank { null },
                    categoriaId = categoriaId // <--- AÑADIDO
                )
                if (response.isSuccessful) {
                    resultados.value = response.body() ?: emptyList()
                }
            } catch (e: Exception) {
                // Manejo de error silencioso o log
            } finally {
                isLoading.value = false
            }
        }
    }
}