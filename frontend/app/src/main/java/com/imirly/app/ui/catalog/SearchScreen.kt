package com.imirly.app.ui.catalog

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(navController: NavController, categoriaId: String? = null) { // <--- AÑADIDO
    val viewModel: SearchViewModel = viewModel()
    val iMirlyPurple = Color(0xFF6C5CE7)

    // Control del diálogo de filtros
    val showFilterDialog = remember { mutableStateOf(false) }
    val tempUbicacion = remember { mutableStateOf(viewModel.ubicacion.value) }

    // Herramienta para forzar el foco y abrir el teclado
    val focusRequester = remember { FocusRequester() }

    // Al entrar a la pantalla, enfocamos la barra de búsqueda y buscamos todo (vacío)
    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
        viewModel.buscar() // Carga inicial
        viewModel.categoriaId = categoriaId
    }

    Column(modifier = Modifier.fillMaxSize().background(Color(0xFFFAFAFA))) {
        // CABECERA DE BÚSQUEDA
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 24.dp, start = 12.dp, end = 24.dp, bottom = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
            }

            OutlinedTextField(
                value = viewModel.query.value,
                onValueChange = { viewModel.onQueryChange(it) },
                modifier = Modifier
                    .weight(1f)
                    .focusRequester(focusRequester), // Obliga a abrir el teclado
                placeholder = { Text("Busca fontanero, limpieza...") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = Color.Gray) },
                trailingIcon = {
                    // Botón para limpiar el texto rápidamente
                    if (viewModel.query.value.isNotEmpty()) {
                        IconButton(onClick = { viewModel.onQueryChange("") }) {
                            Icon(Icons.Default.Clear, contentDescription = "Borrar", tint = Color.Gray)
                        }
                    }
                },
                shape = RoundedCornerShape(16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = iMirlyPurple,
                    unfocusedBorderColor = Color(0xFFEEEEEE),
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White
                ),
                singleLine = true
            )

            Spacer(modifier = Modifier.width(8.dp))

            // Botón de Filtros
            IconButton(onClick = {
                tempUbicacion.value = viewModel.ubicacion.value // Cargamos el valor actual
                showFilterDialog.value = true
            }) {
                // Si hay un filtro aplicado, el icono se pone morado para avisar
                Icon(
                    Icons.Default.Tune,
                    contentDescription = "Filtros",
                    tint = if (viewModel.ubicacion.value.isNotEmpty()) iMirlyPurple else Color.Gray
                )
            }
        }

        // RESULTADOS DE BÚSQUEDA
        if (viewModel.isLoading.value) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = iMirlyPurple)
            }
        } else if (viewModel.hasSearched.value && viewModel.resultados.value.isEmpty()) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("No se encontraron resultados", color = Color.Gray)
            }
        } else {
            LazyColumn(
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(viewModel.resultados.value) { anuncio ->
                    // ¡Reutilizamos tu tarjeta perfecta!
                    AnuncioClienteCard(anuncio) {
                        navController.navigate("anuncio_detail/${anuncio.id}")
                    }
                }
            }
        }
    }
    // DIÁLOGO DE FILTROS
    if (showFilterDialog.value) {
        AlertDialog(
            onDismissRequest = { showFilterDialog.value = false },
            containerColor = Color.White,
            title = { Text("Filtros de Búsqueda", fontWeight = FontWeight.Bold) },
            text = {
                Column {
                    Text("Ubicación", color = Color.Gray, fontSize = 14.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = tempUbicacion.value,
                        onValueChange = { tempUbicacion.value = it },
                        placeholder = { Text("Ej: Madrid, Barcelona...") },
                        leadingIcon = { Icon(Icons.Default.LocationOn, contentDescription = null, tint = Color.Gray) },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = iMirlyPurple)
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.ubicacion.value = tempUbicacion.value // Guardamos el filtro
                        viewModel.buscar() // Relanzamos la búsqueda
                        showFilterDialog.value = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = iMirlyPurple)
                ) {
                    Text("Aplicar")
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    viewModel.ubicacion.value = "" // Limpiamos el filtro
                    viewModel.buscar() // Relanzamos la búsqueda limpia
                    showFilterDialog.value = false
                }) {
                    Text("Limpiar", color = Color.Gray)
                }
            }
        )
    }
}