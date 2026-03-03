package com.imirly.app.network

import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

// El DTO que recibimos del backend
data class CategoriaResponse(
    val id: String,
    val nombre: String,
    val icono: String,
    val numAnuncios: Int? = 0
)

data class CreateAnuncioRequest(
    val subcategoriaId: String,
    val titulo: String,
    val descripcion: String,
    val precioHora: Double,
    val ubicacion: String
)

data class SubcategoriaResponse(
    val id: String,
    val categoriaId: String,
    val nombre: String,
    val icono: String
)

data class AnuncioResponse(
    val id: String,
    val titulo: String,
    val descripcion: String,
    val precioHora: Double,
    val ubicacion: String,
    val activo: Boolean,
    val subcategoriaId: String,
    val nombreProfesional: String? = "Profesional",
    val valoracionProfesional: Double = 0.0,
    val numeroValoracionesProfesional: Int = 0
)

data class UpdateAnuncioRequest(
    val titulo: String,
    val descripcion: String,
    val precioHora: Double,
    val ubicacion: String,
    val activo: Boolean // <--- Añadido
)

data class CheckFavoritoResponse(
    val isFavorito: Boolean
)

interface CatalogApiService {
    @GET("/api/v1/catalog/categories")
    suspend fun getCategorias(): Response<List<CategoriaResponse>>

    // 1. Obtener subcategorías de una categoría específica
    @GET("/api/v1/catalog/categories/{categoriaId}/subcategories")
    suspend fun getSubcategorias(@Path("categoriaId") categoriaId: String): Response<List<SubcategoriaResponse>>

    // 2. Obtener anuncios de una subcategoría específica
    @GET("/api/v1/anuncios/subcategoria/{subcategoriaId}")
    suspend fun getAnunciosBySubcategoria(@Path("subcategoriaId") subcategoriaId: String): Response<List<AnuncioResponse>>

    // NUEVO: Obtener los anuncios del usuario logueado
    @GET("/api/v1/anuncios/mis-anuncios")
    suspend fun getMisAnuncios(): Response<List<AnuncioResponse>>

    @GET("/api/v1/anuncios/{id}")
    suspend fun getAnuncioById(@Path("id") id: String): Response<AnuncioResponse>

    // --- NUEVO: BUSCADOR DE ANUNCIOS ---
    @GET("/api/v1/anuncios/search")
    suspend fun searchAnuncios(
        @Query("q") query: String? = null,
        @Query("ubicacion") ubicacion: String? = null,
        @Query("categoriaId") categoriaId: String? = null // <--- AÑADIDO
    ): Response<List<AnuncioResponse>>

    // CAMBIA ESTA LÍNEA:
    @POST("/api/v1/anuncios")
    suspend fun crearAnuncio(@Body request: CreateAnuncioRequest): Response<ResponseBody>
    @DELETE("/api/v1/anuncios/{id}")
    suspend fun deleteAnuncio(@Path("id") id: String): Response<ResponseBody>

    @PUT("/api/v1/anuncios/{id}")
    suspend fun updateAnuncio(
        @Path("id") id: String,
        @Body request: UpdateAnuncioRequest
    ): Response<AnuncioResponse>

    @POST("/api/v1/favoritos/{anuncioId}")
    suspend fun addFavorito(@Path("anuncioId") anuncioId: String): Response<okhttp3.ResponseBody>

    @DELETE("/api/v1/favoritos/{anuncioId}")
    suspend fun removeFavorito(@Path("anuncioId") anuncioId: String): Response<okhttp3.ResponseBody>

    @GET("/api/v1/favoritos")
    suspend fun getMisFavoritos(): Response<List<AnuncioResponse>>

    @GET("/api/v1/favoritos/{anuncioId}/check")
    suspend fun checkIsFavorito(@Path("anuncioId") anuncioId: String): Response<CheckFavoritoResponse>
}