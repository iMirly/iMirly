package com.imirly.app.ui.catalog

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
fun AnuncioDetailScreen(navController: NavController, anuncioId: String) {
    val viewModel: AnuncioDetailViewModel = viewModel()
    val iMirlyPurple = Color(0xFF6C5CE7)

    LaunchedEffect(anuncioId) { viewModel.cargarDetalle(anuncioId) }

    val anuncio = viewModel.anuncio.value

    Scaffold(
        bottomBar = {
            if (anuncio != null) {
                // Botón Contactar fijado abajo
                Surface(modifier = Modifier.fillMaxWidth().padding(24.dp), color = Color.Transparent) {
                    Button(
                        // 👇 AL PULSAR, ABRIMOS EL DIÁLOGO 👇
                        onClick = { viewModel.showContactDialog.value = true },
                        modifier = Modifier.fillMaxWidth().height(56.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = iMirlyPurple)
                    ) {
                        Text("Contactar", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    ) { innerPadding ->
        if (viewModel.isLoading.value) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = iMirlyPurple)
            }
        } else if (anuncio != null) {
            // 1. Column principal SIN padding para que la flecha quede arriba del todo
            Column(
                modifier = Modifier.fillMaxSize().background(Color(0xFFFAFAFA))
            ) {
                // 2. CABECERA UNIVERSAL ESTANDARIZADA
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 24.dp, start = 12.dp, end = 24.dp, bottom = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver",
                            tint = Color.Black
                        )
                    }
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Detalle del servicio", fontSize = 22.sp, fontWeight = FontWeight.Bold)
                }

                //3. Segunda Column para hacer scroll del contenido con sus márgenes correctos
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState())
                        .padding(horizontal = 24.dp)
                        .padding(bottom = innerPadding.calculateBottomPadding()) // Respetamos el botón de abajo
                ) {
                    Spacer(modifier = Modifier.height(24.dp))

                    // 👇 ESTA COLUMNA INTERNA ES LA CLAVE PARA CENTRAR AVATAR Y PRECIO 👇
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(contentAlignment = Alignment.BottomEnd) {
                            Surface(
                                modifier = Modifier.size(120.dp),
                                shape = CircleShape,
                                color = iMirlyPurple.copy(alpha = 0.8f)
                            ) {
                                Icon(
                                    Icons.Default.Person,
                                    null,
                                    modifier = Modifier.padding(24.dp),
                                    tint = Color.White
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = anuncio.nombreProfesional ?: "Profesional",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold
                        )

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                Icons.Default.Star,
                                null,
                                tint = Color(0xFFFFB800),
                                modifier = Modifier.size(16.dp)
                            )
                            val formatedRating = String.format("%.1f", anuncio.valoracionProfesional).replace(',', '.')
                            Text(" $formatedRating", fontWeight = FontWeight.Bold)
                            Text(" • ${anuncio.numeroValoracionesProfesional} reseñas", color = Color.Gray)
                        }

                        Spacer(modifier = Modifier.height(24.dp))

                        // Badge de Precio Destacado
                        Surface(color = iMirlyPurple, shape = RoundedCornerShape(20.dp)) {
                            Column(
                                modifier = Modifier.padding(
                                    horizontal = 32.dp,
                                    vertical = 12.dp
                                ), horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    "${anuncio.precioHora}€",
                                    color = Color.White,
                                    fontSize = 28.sp,
                                    fontWeight = FontWeight.ExtraBold
                                )
                                Text(
                                    "por hora",
                                    color = Color.White.copy(alpha = 0.8f),
                                    fontSize = 12.sp
                                )
                            }
                        }
                    }
                    // 👆 HASTA AQUÍ EL BLOQUE CENTRADO 👆

                    Spacer(modifier = Modifier.height(32.dp))

                    // 👇 A PARTIR DE AQUÍ TODO SE ALINEA A LA IZQUIERDA AUTOMÁTICAMENTE 👇

                    // Sección Sobre Mí
                    Text("Sobre mí", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(12.dp))
                    Surface(
                        color = Color.White,
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = anuncio.descripcion,
                            modifier = Modifier.padding(20.dp),
                            color = Color.DarkGray,
                            lineHeight = 24.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // Ubicación
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Default.LocationOn,
                            null,
                            tint = iMirlyPurple,
                            modifier = Modifier.size(18.dp)
                        )
                        Text(
                            "  ${anuncio.ubicacion}",
                            color = Color.Gray,
                            fontWeight = FontWeight.Medium
                        )
                    }

                    Spacer(modifier = Modifier.height(100.dp)) // Espacio para el botón de abajo
                }
            }
        }
    }

    // --- DIÁLOGO PARA ESCRIBIR EL MENSAJE DE CONTACTO ---
    if (viewModel.showContactDialog.value) {
        AlertDialog(
            onDismissRequest = { viewModel.showContactDialog.value = false },
            containerColor = Color.White,
            title = { Text("Contactar Profesional", fontWeight = FontWeight.Bold) },
            text = {
                Column {
                    Text("Explica brevemente qué necesitas para que el profesional pueda evaluar tu solicitud.", color = Color.Gray, fontSize = 14.sp)
                    Spacer(modifier = Modifier.height(16.dp))
                    OutlinedTextField(
                        value = viewModel.mensajeContacto.value,
                        onValueChange = { viewModel.mensajeContacto.value = it },
                        placeholder = { Text("Ej: Necesito pintar el salón este sábado por la mañana...") },
                        modifier = Modifier.fillMaxWidth().height(120.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = iMirlyPurple)
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        // 1. Ocultamos la ventana de escribir al instante
                        viewModel.showContactDialog.value = false
                        // 2. Mandamos la orden al backend
                        viewModel.enviarSolicitud(anuncioId)
                    },
                    enabled = viewModel.mensajeContacto.value.isNotBlank() && !viewModel.isSubmitting.value,
                    colors = ButtonDefaults.buttonColors(containerColor = iMirlyPurple)
                ) {
                    if (viewModel.isSubmitting.value) {
                        CircularProgressIndicator(modifier = Modifier.size(20.dp), color = Color.White, strokeWidth = 2.dp)
                    } else {
                        Text("Enviar Solicitud")
                    }
                }
            },
            dismissButton = {
                TextButton(onClick = { viewModel.showContactDialog.value = false }) {
                    Text("Cancelar", color = Color.Gray)
                }
            }
        )
    }

    // --- MENSAJE DE ÉXITO ---
    if (viewModel.requestSuccess.value) {
        AlertDialog(
            onDismissRequest = { viewModel.requestSuccess.value = false },
            containerColor = Color.White,
            icon = { Icon(Icons.Default.CheckCircle, contentDescription = null, tint = Color(0xFF2ECC71), modifier = Modifier.size(48.dp)) },
            title = { Text("¡Solicitud enviada!", textAlign = TextAlign.Center) },
            text = { Text("El profesional revisará tu solicitud y te contestará pronto. Podrás ver el estado en la pestaña de Mensajes.", textAlign = TextAlign.Center) },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.requestSuccess.value = false
                        // Opcional: navController.popBackStack() si quieres que salga del anuncio tras pedirlo
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = iMirlyPurple)
                ) {
                    Text("Entendido")
                }
            }
        )
    }
}