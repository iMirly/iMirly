package com.imirly.app.network

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private const val BASE_URL = "http://10.0.2.2:8080"

    // Creamos un cliente HTTP personalizado que añade el Token automáticamente
    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(Interceptor { chain ->
            val originalRequest = chain.request()
            val token = TokenManager.jwtToken

            // Si tenemos token, lo añadimos a la cabecera "Authorization"
            val newRequest = if (token != null) {
                originalRequest.newBuilder()
                    .header("Authorization", "Bearer $token")
                    .build()
            } else {
                originalRequest
            }

            chain.proceed(newRequest)
        })
        .build()

    val apiService: AuthApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient) // <- Añadimos nuestro cliente interceptor
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(AuthApiService::class.java)
    }

    val catalogService: CatalogApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient) // Usa el cliente con el Token
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(CatalogApiService::class.java)
    }

    val userService: UserApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient) // <--- ¡ESTA LÍNEA ES VITAL! Si falta, da error 403.
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(UserApiService::class.java)
    }

    val solicitudService: SolicitudApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient) // Usa el cliente con el Token
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(SolicitudApiService::class.java)
    }

    val mensajeService: MensajeApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient) // Usa el cliente con el Token
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(MensajeApiService::class.java)
    }
}