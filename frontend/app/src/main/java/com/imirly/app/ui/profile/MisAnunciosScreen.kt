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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MisAnunciosScreen(navController: NavController, viewModel: MisAnunciosViewModel = viewModel()) {
    val iMirlyPurple = Color(0xFF6C5CE7)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mis anuncios", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver",
                            tint = Color.Black
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color(0xFFFAFAFA))
        ) {
            // Selector de Pestañas (Tabs)
            TabRow(
                selectedTabIndex = viewModel.selectedTab.value,
                containerColor = Color.White,
                contentColor = iMirlyPurple,
                indicator = { tabPositions ->
                    TabRowDefaults.SecondaryIndicator(
                        Modifier.tabIndicatorOffset(tabPositions[viewModel.selectedTab.value]),
                        color = iMirlyPurple
                    )
                },
                divider = { HorizontalDivider(color = Color(0xFFF0F0F0)) }
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

                if (listaAMostrar.isEmpty()) {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("No tienes anuncios en esta sección", color = Color.Gray)
                    }
                } else {
                    LazyColumn(
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        items(listaAMostrar) { anuncio ->
                            AnuncioCard(anuncio, viewModel)
                        }
                    }
                }
            }
        }
    }

    // DIÁLOGOS
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

    if (viewModel.showEditDialog.value) {
        AlertDialog(
            onDismissRequest = { viewModel.showEditDialog.value = false },
            title = { Text("Editar Anuncio", fontWeight = FontWeight.Bold) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    OutlinedTextField(value = viewModel.editTitulo.value, onValueChange = { viewModel.editTitulo.value = it }, label = { Text("Título") }, modifier = Modifier.fillMaxWidth())
                    OutlinedTextField(value = viewModel.editPrecio.value, onValueChange = { viewModel.editPrecio.value = it }, label = { Text("Precio/hora (€)") }, modifier = Modifier.fillMaxWidth())
                    OutlinedTextField(value = viewModel.editUbicacion.value, onValueChange = { viewModel.editUbicacion.value = it }, label = { Text("Ubicación") }, modifier = Modifier.fillMaxWidth())
                    OutlinedTextField(
                        value = viewModel.editDescripcion.value,
                        onValueChange = { viewModel.editDescripcion.value = it },
                        label = { Text("Descripción") },
                        modifier = Modifier.fillMaxWidth().height(100.dp)
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Anuncio visible", fontWeight = FontWeight.SemiBold)
                            Text("Si lo desactivas, no aparecerá en el catálogo", fontSize = 11.sp, color = Color.Gray, lineHeight = 14.sp)
                        }
                        Spacer(modifier = Modifier.width(16.dp))
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
fun AnuncioCard(anuncio: AnuncioResponse, viewModel: MisAnunciosViewModel) {
    val iMirlyPurple = Color(0xFF6C5CE7)

    Surface(
        shape = RoundedCornerShape(20.dp),
        color = Color.White,
        shadowElevation = 2.dp
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
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

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.LocationOn, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(12.dp))
                        Spacer(Modifier.width(4.dp))
                        Text(anuncio.ubicacion, color = Color.Gray, fontSize = 12.sp)
                    }

                    Text("${anuncio.precioHora}€/hora", color = iMirlyPurple, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
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

                IconButton(onClick = { viewModel.prepararEdicion(anuncio) }) {
                    Icon(Icons.Outlined.Edit, contentDescription = "Editar", tint = iMirlyPurple)
                }
                IconButton(onClick = { viewModel.confirmarBorrado(anuncio) }) {
                    Icon(Icons.Outlined.Delete, contentDescription = "Eliminar", tint = Color(0xFFFF4757))
                }
            }
        }
    }
}