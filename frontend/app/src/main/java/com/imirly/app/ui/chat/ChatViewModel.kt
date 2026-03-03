package com.imirly.app.ui.chat

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.imirly.app.network.EnviarMensajeRequest
import com.imirly.app.network.MensajeResponse
import com.imirly.app.network.RetrofitClient
import com.imirly.app.network.TokenManager
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class ChatViewModel : ViewModel() {
    var mensajes = mutableStateOf<List<MensajeResponse>>(emptyList())
    var inputText = mutableStateOf("")

    // Necesitamos saber quiénes somos para pintar la burbuja a la derecha o a la izquierda
    var myUserId = mutableStateOf<String?>(null)

    private var pollingJob: Job? = null

    init {
        myUserId.value = TokenManager.getUserId()
    }

    // Arrancamos el "refresco automático" cada 3 segundos
    fun startPolling(otroUsuarioId: String) {
        pollingJob?.cancel()
        pollingJob = viewModelScope.launch {
            while (isActive) {
                cargarConversacion(otroUsuarioId)
                delay(3000) // Espera 3 segundos y vuelve a preguntar
            }
        }
    }

    // Cuando salgamos de la pantalla, apagamos el refresco para ahorrar batería
    fun stopPolling() {
        pollingJob?.cancel()
    }

    private suspend fun cargarConversacion(otroUsuarioId: String) {
        try {
            val response = RetrofitClient.mensajeService.obtenerConversacion(otroUsuarioId)
            if (response.isSuccessful) {
                mensajes.value = response.body() ?: emptyList()
            }
        } catch (e: Exception) {
            android.util.Log.e("ChatViewModel", "Error al enviar mensaje", e)        }
    }

    fun enviarMensaje(receptorId: String) {
        val texto = inputText.value.trim()
        if (texto.isEmpty()) return

        // Vaciamos la caja de texto al instante para dar feedback rápido
        inputText.value = ""

        viewModelScope.launch {
            try {
                val request = EnviarMensajeRequest(receptorId, texto)
                val response = RetrofitClient.mensajeService.enviarMensaje(request)
                if (response.isSuccessful) {
                    cargarConversacion(receptorId) // Recargamos al instante para ver nuestro mensaje
                }
            } catch (e: Exception) {
                android.util.Log.e("ChatViewModel", "Error al enviar mensaje", e)            }
        }
    }

    // NUEVA FUNCIÓN: Envía un mensaje especial de tipo presupuesto
    fun enviarPresupuesto(receptorId: String, solicitudId: String, presupuestoContenido: String) {
        if (presupuestoContenido.isBlank() || presupuestoContenido.startsWith("|")) return

        viewModelScope.launch {
            try {
                // Creamos un Request especial con el tipo PROPUESTA_PRECIO y atado a la solicitud
                val request = EnviarMensajeRequest(
                    receptorId = receptorId,
                    contenido = presupuestoContenido, // El backend espera que el contenido sea el número|descripción
                    solicitudId = solicitudId,
                    tipo = "PROPUESTA_PRECIO"
                )
                val response = RetrofitClient.mensajeService.enviarMensaje(request)
                if (response.isSuccessful) {
                    cargarConversacion(receptorId)
                }
            } catch (e: Exception) {
                android.util.Log.e("ChatViewModel", "Error al enviar presupuesto", e)
            }
        }
    }

    fun rechazarPresupuesto(solicitudId: String, otroUsuarioId: String) {
        viewModelScope.launch {
            try {
                val requestEstado = com.imirly.app.network.CambiarEstadoRequest("ACEPTADO")
                val response = RetrofitClient.solicitudService.cambiarEstadoSolicitud(solicitudId, requestEstado)

                if (response.isSuccessful) {
                    val requestMsg = EnviarMensajeRequest(
                        receptorId = otroUsuarioId,
                        contenido = "He rechazado la oferta. Por favor, revisa el precio y envíame un nuevo presupuesto.",
                        solicitudId = solicitudId,
                        tipo = "PRESUPUESTO_RECHAZADO"
                    )
                    // 👇 Añadimos control de error real al enviar el mensaje 👇
                    val msgResponse = RetrofitClient.mensajeService.enviarMensaje(requestMsg)
                    if (!msgResponse.isSuccessful) {
                        android.util.Log.e("ChatViewModel", "Fallo al enviar mensaje de rechazo: ${msgResponse.code()} - ${msgResponse.errorBody()?.string()}")
                    }

                    cargarConversacion(otroUsuarioId)
                }
            } catch (e: Exception) {
                android.util.Log.e("ChatViewModel", "Excepción al rechazar", e)
            }
        }
    }

    fun borrarChatYSolicitud(otroUsuarioId: String, solicitudId: String, onSuccess: () -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch {
            try {
                // 1. Intentamos borrar la solicitud primero (el backend la protegerá si hay dinero)
                val response = RetrofitClient.solicitudService.eliminarSolicitud(solicitudId)

                if (response.isSuccessful) {
                    // 2. Si nos deja, borramos los mensajes
                    RetrofitClient.mensajeService.borrarConversacion(otroUsuarioId)
                    onSuccess()
                } else {
                    // El backend ha bloqueado el borrado (ej. error 400)
                    onError("No se puede borrar este chat porque hay un presupuesto en curso o pagado.")
                }
            } catch (e: Exception) {
                onError("Error de conexión al intentar borrar el chat.")
            }
        }
    }

    fun aceptarYPagar(solicitudId: String, otroUsuarioId: String) {
        viewModelScope.launch {
            try {
                val responsePago = RetrofitClient.solicitudService.pagarSolicitud(solicitudId)

                if (responsePago.isSuccessful) {
                    val requestMsg = EnviarMensajeRequest(
                        receptorId = otroUsuarioId,
                        contenido = "He aceptado el presupuesto y he realizado el pago. El dinero está retenido de forma segura en iMirly. Ya puedes empezar el trabajo.",
                        solicitudId = solicitudId,
                        tipo = "PRESUPUESTO_ACEPTADO"
                    )
                    // 👇 Añadimos control de error real al enviar el mensaje 👇
                    val msgResponse = RetrofitClient.mensajeService.enviarMensaje(requestMsg)
                    if (!msgResponse.isSuccessful) {
                        android.util.Log.e("ChatViewModel", "Fallo al enviar mensaje de pago: ${msgResponse.code()} - ${msgResponse.errorBody()?.string()}")
                    }

                    cargarConversacion(otroUsuarioId)
                }
            } catch (e: Exception) {
                android.util.Log.e("ChatViewModel", "Excepción crítica al pagar", e)
            }
        }
    }

    fun marcarTrabajoFinalizado(receptorId: String, solicitudId: String) {
        viewModelScope.launch {
            try {
                val requestMsg = EnviarMensajeRequest(
                    receptorId = receptorId,
                    contenido = "He marcado el trabajo como finalizado. Por favor, revísalo y confirma si todo está correcto para liberar el pago.",
                    solicitudId = solicitudId,
                    tipo = "TRABAJO_FINALIZADO"
                )
                val msgResponse = RetrofitClient.mensajeService.enviarMensaje(requestMsg)
                if (msgResponse.isSuccessful) cargarConversacion(receptorId)
            } catch (e: Exception) {
                android.util.Log.e("ChatViewModel", "Excepción al finalizar trabajo", e)
            }
        }
    }

    fun aprobarTrabajo(receptorId: String, solicitudId: String) {
        viewModelScope.launch {
            try {
                val requestMsg = EnviarMensajeRequest(
                    receptorId = receptorId,
                    contenido = "He revisado el trabajo y está perfecto. Pago liberado, ¡muchas gracias!",
                    solicitudId = solicitudId,
                    tipo = "SERVICIO_COMPLETADO"
                )
                val msgResponse = RetrofitClient.mensajeService.enviarMensaje(requestMsg)
                if (msgResponse.isSuccessful) cargarConversacion(receptorId)
            } catch (e: Exception) {
                android.util.Log.e("ChatViewModel", "Excepción al aprobar trabajo", e)
            }
        }
    }

    fun rechazarTrabajo(receptorId: String, solicitudId: String) {
        viewModelScope.launch {
            try {
                val requestMsg = EnviarMensajeRequest(
                    receptorId = receptorId,
                    contenido = "El trabajo no cumple con lo acordado. Necesita arreglos.",
                    solicitudId = solicitudId,
                    tipo = "SERVICIO_RECHAZADO"
                )
                val msgResponse = RetrofitClient.mensajeService.enviarMensaje(requestMsg)
                if (msgResponse.isSuccessful) cargarConversacion(receptorId)
            } catch (e: Exception) {
                android.util.Log.e("ChatViewModel", "Excepción al rechazar trabajo", e)
            }
        }
    }

    fun enviarValoracion(receptorId: String, solicitudId: String, nota: Double) {
        viewModelScope.launch {
            try {
                val requestMsg = EnviarMensajeRequest(
                    receptorId = receptorId,
                    contenido = "$nota|He dejado una valoración de $nota estrellas.",
                    solicitudId = solicitudId,
                    tipo = "VALORACION"
                )
                val msgResponse = RetrofitClient.mensajeService.enviarMensaje(requestMsg)
                if (msgResponse.isSuccessful) cargarConversacion(receptorId)
            } catch (e: Exception) {
                android.util.Log.e("ChatViewModel", "Excepción al valorar", e)
            }
        }
    }
}