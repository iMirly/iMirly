package com.imirly.app.ui.profile

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.imirly.app.network.RetrofitClient
import com.imirly.app.network.TokenManager
import com.imirly.app.network.TransaccionDTO
import kotlinx.coroutines.launch

class SaldoViewModel : ViewModel() {
    var saldo = mutableStateOf(0.0)
    var pendiente = mutableStateOf(0.0)
    var esteMes = mutableStateOf(0.0)
    var transacciones = mutableStateOf<List<TransaccionDTO>>(emptyList())
    var isLoading = mutableStateOf(true)

    init {
        cargarSaldo()
    }

    private fun cargarSaldo() {
        val currentEmail = TokenManager.getUserId() ?: return
        viewModelScope.launch {
            try {
                val response = RetrofitClient.userService.getWallet()
                if (response.isSuccessful) {
                    val body = response.body()
                    if (body != null) {
                        saldo.value = body.disponible
                        pendiente.value = body.pendiente
                        esteMes.value = body.esteMes
                        transacciones.value = body.recientes
                    }
                }
            } catch (e: Exception) {
                // Error handled silently for UI purposes
            } finally {
                isLoading.value = false
            }
        }
    }

    fun retirarSaldo() {
        viewModelScope.launch {
            try {
                val response = RetrofitClient.userService.retirarSaldo()
                if (response.isSuccessful) {
                    cargarSaldo() // Recargar el saldo actualizado
                }
            } catch (e: Exception) {
                // Error handled silently
            }
        }
    }

    fun ingresarSaldo() {
        viewModelScope.launch {
            try {
                val response = RetrofitClient.userService.ingresarSaldo()
                if (response.isSuccessful) {
                    cargarSaldo() // Recargar el saldo actualizado
                }
            } catch (e: Exception) {
                // Error handled silently
            }
        }
    }
}
