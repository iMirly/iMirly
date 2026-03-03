package com.imirly.app.ui.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.Composable
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
fun MisAnunciosScreen(navController: NavController, viewModel: MisAnunciosViewModel = viewModel()) {
    val iMirlyPurple = Color(0xFF6C5CE7)

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
            Text("Mis anuncios", fontSize = 22.sp, fontWeight = FontWeight.Bold)
        }

        // Selector de Pestañas (Tabs)
        TabRow(
            selectedTabIndex = viewModel.selectedTab.value,
            containerColor = Color.Transparent,
            contentColor = iMirlyPurple,
            indicator = { tabPositions ->
                TabRowDefaults.SecondaryIndicator(
                    Modifier.tabIndicatorOffset(tabPositions[viewModel.selectedTab.value]),
                    color = iMirlyPurple
                )
            },
            divider = {}
        ) {
            Tab(
                selected = viewModel.selectedTab.value == 0,
                onClick = { viewModel.selectedTab.value = 0 },
                text = { Text("Activos (${viewModel.anunciosActivos.value.size})") }
            )
            Tab(
                selected = viewModel.selectedTab.value == 1,
                onClick = { viewModel.selectedTab.value = 1 },
                text = { Text("Inactivos (${viewModel.anunciosInactivos.value.size})") }
            )
        }

        if (viewModel.isLoading.value) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = iMirlyPurple)
            }
        } else {
            val listaAMostrar = if (viewModel.selectedTab.value == 0) viewModel.anunciosActivos.value else viewModel.anunciosInactivos.value

            LazyColumn(
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(listaAMostrar) { anuncio ->
                    AnuncioCard(anuncio)
                }
            }
        }
    }
    // Al final de MisAnunciosScreen, antes de cerrar el bloque principal:

    // 1. DIÁLOGO DE BORRADO
    if (viewModel.showDeleteConfirm.value) {
        AlertDialog(
            onDismissRequest = { viewModel.showDeleteConfirm.value = false },
            title = { Text("¿Eliminar anuncio?") },
            text = { Text("Esta acción no se puede deshacer. El anuncio desaparecerá del catálogo.") },
            confirmButton = {
                TextButton(onClick = { viewModel.eliminarAnuncio() }) {
                    Text("Eliminar", color = Color(0xFFFF4757), fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { viewModel.showDeleteConfirm.value = false }) {
                    Text("Cancelar")
                }
            }
        )
    }

    // 2. DIÁLOGO DE EDICIÓN (Siguiendo el estilo de Nuevo Anuncio)
    if (viewModel.showEditDialog.value) {
        AlertDialog(
            onDismissRequest = { viewModel.showEditDialog.value = false },
            title = { Text("Editar Anuncio", fontWeight = FontWeight.Bold) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    OutlinedTextField(value = viewModel.editTitulo.value, onValueChange = { viewModel.editTitulo.value = it }, label = { Text("Título") })
                    OutlinedTextField(value = viewModel.editPrecio.value, onValueChange = { viewModel.editPrecio.value = it }, label = { Text("Precio/hora (€)") })
                    OutlinedTextField(value = viewModel.editUbicacion.value, onValueChange = { viewModel.editUbicacion.value = it }, label = { Text("Ubicación") })
                    OutlinedTextField(
                        value = viewModel.editDescripcion.value,
                        onValueChange = { viewModel.editDescripcion.value = it },
                        label = { Text("Descripción") },
                        modifier = Modifier.height(100.dp)
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text("Anuncio visible", fontWeight = FontWeight.SemiBold)
                            Text("Si lo desactivas, no aparecerá en el catálogo", fontSize = 12.sp, color = Color.Gray)
                        }
                        Switch(
                            checked = viewModel.editActivo.value,
                            onCheckedChange = { viewModel.editActivo.value = it },
                            colors = SwitchDefaults.colors(checkedThumbColor = iMirlyPurple)
                        )
                    }
                }
            },
            confirmButton = {
                Button(onClick = { viewModel.actualizarAnuncio() }, colors = ButtonDefaults.buttonColors(containerColor = iMirlyPurple)) {
                    Text("Guardar cambios")
                }
            }
        )
    }
}

@Composable
fun AnuncioCard(anuncio: AnuncioResponse, viewModel: MisAnunciosViewModel = viewModel()) {
    val iMirlyPurple = Color(0xFF6C5CE7)

    Surface(
        shape = RoundedCornerShape(20.dp),
        color = Color.White,
        shadowElevation = 2.dp
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                // Placeholder de imagen
                Surface(
                    modifier = Modifier.size(80.dp),
                    shape = RoundedCornerShape(12.dp),
                    color = Color(0xFFF0F0F0)
                ) {
                    Icon(Icons.Default.Image, contentDescription = null, tint = Color.LightGray, modifier = Modifier.padding(20.dp))
                }

                Spacer(modifier = Modifier.width(16.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(anuncio.titulo, fontWeight = FontWeight.Bold, fontSize = 16.sp)

                    // Nueva fila para la ubicación
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.LocationOn, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(12.dp))
                        Spacer(Modifier.width(4.dp))
                        Text(anuncio.ubicacion, color = Color.Gray, fontSize = 12.sp)
                    }

                    Text("${anuncio.precioHora}€/hora", color = iMirlyPurple, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                }

                IconButton(onClick = { /* Menú de opciones */ }) {
                    Icon(Icons.Default.MoreVert, contentDescription = null, tint = Color.LightGray)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                // Badge de Estado
                Surface(
                    color = if (anuncio.activo) Color(0xFFE8F5E9) else Color(0xFFF5F5F5),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Row(Modifier.padding(horizontal = 8.dp, vertical = 4.dp), verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            if (anuncio.activo) Icons.Default.CheckCircle else Icons.Default.Pause,
                            contentDescription = null,
                            tint = if (anuncio.activo) Color(0xFF2ECC71) else Color.Gray,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(Modifier.width(4.dp))
                        Text(
                            if (anuncio.activo) "Activo" else "Inactivo",
                            color = if (anuncio.activo) Color(0xFF2ECC71) else Color.Gray,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Spacer(Modifier.weight(1f))

                // Botones de acción
                // En MisAnunciosScreen.kt, dentro de AnuncioCard:
                IconButton(onClick = { viewModel.prepararEdicion(anuncio) }) { // <--- Edición
                    Icon(Icons.Outlined.Edit, contentDescription = null, tint = iMirlyPurple)
                }
                IconButton(onClick = { viewModel.confirmarBorrado(anuncio) }) { // <--- Borrado
                    Icon(Icons.Outlined.Delete, contentDescription = null, tint = Color(0xFFFF4757))
                }
            }
        }
    }
}