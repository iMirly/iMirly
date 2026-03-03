package com.imirly.app.ui.publish

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Image
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PublishScreen(onPublishSuccess: () -> Unit, viewModel: PublishViewModel = viewModel()) {
    val iMirlyPurple = Color(0xFF6C5CE7)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .verticalScroll(rememberScrollState())
            .padding(24.dp)
    ) {
        // Cabecera dinámica de Publicar
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            if (viewModel.currentStep.value == 2) {
                IconButton(onClick = { viewModel.currentStep.value = 1 }, modifier = Modifier.offset(x = (-12).dp)) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, null)
                }
            } else {
                // Quitamos la X y dejamos un espacio invisible para que el título se centre perfecto
                Spacer(modifier = Modifier.width(48.dp))
            }

            Text(
                "Nuevo Anuncio",
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )

            Spacer(modifier = Modifier.width(48.dp)) // Contrapeso derecho
        }

        // Indicador de progreso
        LinearProgressIndicator(
            progress = { if (viewModel.currentStep.value == 1) 0.5f else 1f },
            modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp).height(6.dp),
            color = iMirlyPurple,
            trackColor = Color(0xFFF0F0F0)
        )
        Text(
            "Paso ${viewModel.currentStep.value} de 2",
            fontSize = 12.sp,
            color = Color.Gray,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        Spacer(modifier = Modifier.height(24.dp))

        if (viewModel.currentStep.value == 1) {
            // --- PASO 1: DATOS DEL SERVICIO ---
            Surface(
                modifier = Modifier.fillMaxWidth().height(150.dp),
                shape = RoundedCornerShape(16.dp),
                color = Color(0xFFFAFAFA),
                border = BorderStroke(1.dp, Color(0xFFEEEEEE))
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
                    Icon(Icons.Default.Image, null, tint = iMirlyPurple, modifier = Modifier.size(40.dp))
                    Text("Subir imagen", fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
                    Text("JPG, PNG o GIF (máx. 5MB)", color = Color.Gray, fontSize = 10.sp)
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            PublishTextField("Título del servicio", viewModel.titulo.value) { viewModel.titulo.value = it }
            PublishTextField("Precio por hora (€)", viewModel.precio.value) { viewModel.precio.value = it }
            PublishTextField("Ubicación (Ciudad/Barrio)", viewModel.ubicacion.value) { viewModel.ubicacion.value = it }

            OutlinedTextField(
                value = viewModel.descripcion.value,
                onValueChange = { viewModel.descripcion.value = it },
                label = { Text("Descripción del servicio") },
                placeholder = { Text("Describe tu experiencia y lo que ofreces...") },
                modifier = Modifier.fillMaxWidth().height(120.dp),
                shape = RoundedCornerShape(12.dp)
            )

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = { viewModel.currentStep.value = 2 },
                modifier = Modifier.fillMaxWidth().height(56.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = iMirlyPurple),
                enabled = viewModel.titulo.value.isNotEmpty() && viewModel.precio.value.isNotEmpty()
            ) {
                Text("Continuar", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            }
        } else {
            // --- PASO 2: CLASIFICACIÓN ---
            Text("Clasifica tu servicio", fontWeight = FontWeight.Bold, fontSize = 18.sp)
            Text("Selecciona dónde quieres que aparezca tu anuncio", color = Color.Gray, fontSize = 14.sp)

            Spacer(modifier = Modifier.height(24.dp))

            // Selector A: Categorías Principales
            Text("1. Selecciona una categoría", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = iMirlyPurple)
            Row(
                modifier = Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()).padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                viewModel.categorias.value.forEach { cat ->
                    FilterChip(
                        selected = viewModel.categoriaSeleccionadaId.value == cat.id,
                        onClick = { viewModel.cargarSubcategorias(cat.id) },
                        label = { Text(cat.nombre) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = iMirlyPurple.copy(alpha = 0.1f),
                            selectedLabelColor = iMirlyPurple
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Selector B: Subcategorías
            Text("2. Selecciona la especialidad", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = iMirlyPurple)
            if (viewModel.subcategorias.value.isEmpty()) {
                Box(Modifier.fillMaxWidth().padding(24.dp), contentAlignment = Alignment.Center) {
                    Text("Selecciona una categoría arriba para ver opciones", color = Color.LightGray, textAlign = TextAlign.Center)
                }
            } else {
                viewModel.subcategorias.value.forEach { sub ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { viewModel.subcategoriaId.value = sub.id }
                            .padding(vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = viewModel.subcategoriaId.value == sub.id,
                            onClick = { viewModel.subcategoriaId.value = sub.id },
                            colors = RadioButtonDefaults.colors(selectedColor = iMirlyPurple)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(sub.nombre, fontSize = 16.sp)
                    }
                    HorizontalDivider(color = Color(0xFFF5F5F5))
                }
            }

            if (viewModel.errorMensaje.value.isNotEmpty()) {
                Text(viewModel.errorMensaje.value, color = Color.Red, fontSize = 12.sp, modifier = Modifier.padding(top = 8.dp))
            }

            Spacer(modifier = Modifier.height(40.dp))

            Button(
                onClick = {
                    viewModel.publicarAnuncio {
                        onPublishSuccess() // Llamamos a la magia de navegación que configuraremos ahora
                    }
                },
                modifier = Modifier.fillMaxWidth().height(56.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = iMirlyPurple),
                enabled = !viewModel.isLoading.value && viewModel.subcategoriaId.value.isNotEmpty()
            ) {
                if (viewModel.isLoading.value) {
                    CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                } else {
                    Text("Publicar Anuncio", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                }
            }
        }

        Spacer(modifier = Modifier.height(50.dp)) // Espacio final para scroll
    }
}

@Composable
fun PublishTextField(label: String, value: String, onValueChange: (String) -> Unit) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
        shape = RoundedCornerShape(12.dp),
        singleLine = true
    )
}