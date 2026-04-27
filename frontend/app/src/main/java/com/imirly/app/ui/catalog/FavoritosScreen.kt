package com.imirly.app.ui.catalog

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController

@Composable
fun FavoritosScreen(navController: NavController) {
    val viewModel: FavoritosViewModel = viewModel()
    val pinkHeart = Color(0xFFFF2A70)
    val pinkBackground = Color(0xFFFFE5EC)
    var showUnfavoriteDialog by remember { mutableStateOf(false) }
    var selectedAnuncioId by remember { mutableStateOf<String?>(null) }

    // Cargamos los favoritos cada vez que se abre la pantalla
    LaunchedEffect(Unit) {
        viewModel.cargarFavoritos()
    }

    Column(modifier = Modifier.fillMaxSize().background(Color(0xFFFAFAFA))) {
        // Cabecera
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
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(Icons.Default.FavoriteBorder, null, modifier = Modifier.size(64.dp), tint = Color.LightGray)
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("Aún no tienes ningún favorito", color = Color.Gray, fontSize = 16.sp)
                }
            }
        } else {
            LazyColumn(
                contentPadding = PaddingValues(horizontal = 24.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(viewModel.favoritos.value) { anuncio ->
                    AnuncioClienteCard(
                        anuncio = anuncio,
                        isFavorito = true,
                        onFavoriteClick = {
                            selectedAnuncioId = anuncio.id
                            showUnfavoriteDialog = true
                        }
                    ) {
                        navController.navigate("anuncio_detail/${anuncio.id}")
                    }
                }
            }
        }
    }

    // DIÁLOGO BONITO PARA CANCELAR FAVORITO
    if (showUnfavoriteDialog) {
        AlertDialog(
            onDismissRequest = { showUnfavoriteDialog = false },
            containerColor = Color.White,
            shape = RoundedCornerShape(28.dp),
            icon = {
                Icon(Icons.Default.FavoriteBorder, contentDescription = null, tint = pinkHeart, modifier = Modifier.size(40.dp))
            },
            title = {
                Text(
                    text = "¿Quitar de favoritos?",
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            },
            text = {
                Text(
                    "Este profesional dejará de aparecer en tu lista de guardados.",
                    textAlign = TextAlign.Center,
                    color = Color.Gray,
                    modifier = Modifier.fillMaxWidth()
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        selectedAnuncioId?.let { viewModel.quitarFavorito(it) }
                        showUnfavoriteDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = pinkHeart),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp)
                ) {
                    Text("Sí, quitar", fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showUnfavoriteDialog = false },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Cancelar", color = Color.Gray)
                }
            }
        )
    }
}