package com.imirly.app.network

import android.util.Base64
import org.json.JSONObject

object TokenManager {
    var jwtToken: String? = null

    // Función para leer el ID del usuario (el "Subject") desde el JWT
    fun getUserId(): String? {
        val token = jwtToken ?: return null
        return try {
            val parts = token.split(".")
            if (parts.size != 3) return null
            // Decodificamos la parte central del token (el Payload)
            val payload = String(Base64.decode(parts[1], Base64.URL_SAFE))
            val json = JSONObject(payload)
            json.getString("sub") // En Spring Security, 'sub' suele guardar el ID o email
        } catch (e: Exception) {
            null
        }
    }
}