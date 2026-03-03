package com.imirly.app.ui.catalog

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
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

@Composable
fun FavoritosScreen(navController: NavController) {
    val viewModel: FavoritosViewModel = viewModel()
    val pinkHeart = Color(0xFFFF2A70)
    val pinkBackground = Color(0xFFFFE5EC)

    // Cargamos los favoritos cada vez que se abre la pantalla
    LaunchedEffect(Unit) {
        viewModel.cargarFavoritos()
    }

    Column(modifier = Modifier.fillMaxSize().background(Color(0xFFFAFAFA))) {
        // Cabecera igual a tu diseño
        Row(
            modifier = Modifier.fillMaxWidth().padding(24.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text("Favoritos", fontSize = 28.sp, fontWeight = FontWeight.ExtraBold)
                Text("${viewModel.favoritos.value.size} profesionales guardados", color = Color.Gray, fontSize = 14.sp)
            }
            Surface(shape = CircleShape, color = pinkBackground, modifier = Modifier.size(48.dp)) {
                Icon(Icons.Default.Favorite, null, tint = pinkHeart, modifier = Modifier.padding(12.dp))
            }
        }

        if (viewModel.isLoading.value) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = Color(0xFF6C5CE7))
            }
        } else if (viewModel.favoritos.value.isEmpty()) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Aún no tienes ningún profesional favorito.", color = Color.Gray)
            }
        } else {
            LazyColumn(
                contentPadding = PaddingValues(horizontal = 24.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(viewModel.favoritos.value) { anuncio ->
                    // Reutilizamos tu tarjeta, forzamos que el corazón esté lleno y le damos la acción de quitar
                    AnuncioClienteCard(
                        anuncio = anuncio,
                        isFavorito = true,
                        onFavoriteClick = { viewModel.quitarFavorito(anuncio.id) }
                    ) {
                        navController.navigate("anuncio_detail/${anuncio.id}")
                    }
                }
            }
        }
    }
}