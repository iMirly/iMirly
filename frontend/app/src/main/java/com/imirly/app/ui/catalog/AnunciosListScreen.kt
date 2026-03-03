package com.imirly.app.ui.catalog

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.imirly.app.network.AnuncioResponse

@Composable
fun AnunciosListScreen(navController: NavController, subcategoriaId: String, subcategoriaNombre: String) {
    val viewModel: AnunciosListViewModel = viewModel()
    val iMirlyPurple = Color(0xFF6C5CE7)

    LaunchedEffect(subcategoriaId) {
        viewModel.cargarAnuncios(subcategoriaId)
    }

    Column(modifier = Modifier.fillMaxSize().background(Color(0xFFFAFAFA))) {
        // CABECERA UNIVERSAL ESTANDARIZADA
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 24.dp, start = 12.dp, end = 24.dp, bottom = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // El IconButton ya tiene 12dp de margen interno, por eso el start de la Row es 12dp (12+12=24dp perfectos)
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver", tint = Color.Black)
            }
            Spacer(modifier = Modifier.width(4.dp))
            // Título: "Mis anuncios" o la variable `subcategoriaNombre`
            Text(subcategoriaNombre, fontSize = 22.sp, fontWeight = FontWeight.Bold)
        }

        if (viewModel.isLoading.value) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = iMirlyPurple)
            }
        } else {
            LazyColumn(
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(viewModel.anuncios.value) { anuncio ->

                    // 👇 NUEVO: Comprobamos si este anuncio está en nuestra lista de favoritos 👇
                    val isFav = viewModel.favoritosIds.value.contains(anuncio.id)

                    AnuncioClienteCard(
                        anuncio = anuncio,
                        isFavorito = isFav, // <--- Le pasamos el estado
                        onFavoriteClick = { viewModel.toggleFavorito(anuncio.id) } // <--- Le enchufamos la acción
                    ) {
                        navController.navigate("anuncio_detail/${anuncio.id}")
                    }
                }
            }
        }
    }
}

// 3. LA TARJETA (VERSIÓN CLIENTE): Sin botones de edición
@Composable
fun AnuncioClienteCard(
    anuncio: AnuncioResponse,
    isFavorito: Boolean = false, // <--- NUEVO
    onFavoriteClick: () -> Unit = {}, // <--- NUEVO
    onClick: () -> Unit
) {
    val iMirlyPurple = Color(0xFF6C5CE7)
    val pinkHeart = Color(0xFFFF2A70) // El rosa de tu diseño
    val pinkBackground = Color(0xFFFFE5EC) // El fondo rosita del botón

    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(20.dp),
        color = Color.White,
        shadowElevation = 2.dp,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                // Avatar
                Surface(
                    modifier = Modifier.size(70.dp),
                    shape = RoundedCornerShape(12.dp),
                    color = Color(0xFFF0F0F0)
                ) {
                    Icon(Icons.Default.Image, contentDescription = null, tint = Color.LightGray, modifier = Modifier.padding(15.dp))
                }

                Spacer(modifier = Modifier.width(16.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(text = anuncio.nombreProfesional ?: "Profesional", fontWeight = FontWeight.ExtraBold, fontSize = 18.sp)
                    Text(anuncio.titulo, fontWeight = FontWeight.Medium, fontSize = 14.sp, color = Color.DarkGray, maxLines = 1)

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Star, contentDescription = null, tint = Color(0xFFFFB800), modifier = Modifier.size(14.dp))
                        val formatedRating = String.format("%.1f", anuncio.valoracionProfesional).replace(',', '.')
                        Text(" $formatedRating", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        Text(" • ${anuncio.numeroValoracionesProfesional} reseñas", fontSize = 12.sp, color = Color.Gray)
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.LocationOn, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(12.dp))
                        Text(" ${anuncio.ubicacion}", color = Color.Gray, fontSize = 12.sp)
                    }
                }

                Column(horizontalAlignment = Alignment.End) {
                    Text("${anuncio.precioHora}€", color = iMirlyPurple, fontWeight = FontWeight.ExtraBold, fontSize = 18.sp)
                    Text("por hora", color = Color.Gray, fontSize = 10.sp)

                    Spacer(modifier = Modifier.height(8.dp))

                    // 👇 BOTÓN DEL CORAZÓN ANIMADO 👇
                    Surface(
                        shape = CircleShape,
                        color = if (isFavorito) pinkBackground else Color.Transparent,
                        modifier = Modifier.size(36.dp).clickable { onFavoriteClick() }
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = if (isFavorito) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                                contentDescription = "Favorito",
                                tint = if (isFavorito) pinkHeart else Color.LightGray,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))
            Text(text = anuncio.descripcion, fontSize = 13.sp, color = Color.DarkGray, maxLines = 2, lineHeight = 18.sp)
        }
    }
}