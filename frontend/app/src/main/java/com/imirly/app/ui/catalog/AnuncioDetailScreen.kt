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
import androidx.compose.material.icons.filled.Info
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
                Surface(modifier = Modifier.fillMaxWidth().padding(24.dp), color = Color.Transparent) {
                    if (viewModel.esMiAnuncio.value) {
                        Button(
                            onClick = { },
                            enabled = false,
                            modifier = Modifier.fillMaxWidth().height(56.dp),
                            shape = RoundedCornerShape(16.dp),
                            colors = ButtonDefaults.buttonColors(
                                disabledContainerColor = Color(0xFFF0F0F0),
                                disabledContentColor = Color.Gray
                            )
                        ) {
                            Icon(Icons.Default.Info, null, modifier = Modifier.size(20.dp))
                            Spacer(Modifier.width(8.dp))
                            Text("Tu propio anuncio", fontSize = 16.sp)
                        }
                    } else if (viewModel.yaContactado.value) {
                        Button(
                            onClick = { },
                            enabled = false,
                            modifier = Modifier.fillMaxWidth().height(56.dp),
                            shape = RoundedCornerShape(16.dp),
                            colors = ButtonDefaults.buttonColors(
                                disabledContainerColor = Color(0xFFE8F5E9),
                                disabledContentColor = Color(0xFF2ECC71)
                            )
                        ) {
                            Icon(Icons.Default.CheckCircle, null, modifier = Modifier.size(20.dp))
                            Spacer(Modifier.width(8.dp))
                            Text("Solicitud ya enviada", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                        }
                    } else {
                        Button(
                            onClick = { viewModel.showContactDialog.value = true },
                            modifier = Modifier.fillMaxWidth().height(56.dp),
                            shape = RoundedCornerShape(16.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = iMirlyPurple),
                            enabled = !viewModel.isSubmitting.value
                        ) {
                            if (viewModel.isSubmitting.value) {
                                CircularProgressIndicator(modifier = Modifier.size(24.dp), color = Color.White, strokeWidth = 2.dp)
                            } else {
                                Text("Contactar", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                            }
                        }
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
            Column(
                modifier = Modifier.fillMaxSize().background(Color(0xFFFAFAFA))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 24.dp, start = 12.dp, end = 24.dp, bottom = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver", tint = Color.Black)
                    }
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Detalle del servicio", fontSize = 22.sp, fontWeight = FontWeight.Bold)
                }

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState())
                        .padding(horizontal = 24.dp)
                        .padding(bottom = innerPadding.calculateBottomPadding())
                ) {
                    Spacer(modifier = Modifier.height(24.dp))

                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Surface(
                            modifier = Modifier.size(120.dp),
                            shape = CircleShape,
                            color = iMirlyPurple.copy(alpha = 0.8f)
                        ) {
                            Icon(Icons.Default.Person, null, modifier = Modifier.padding(24.dp), tint = Color.White)
                        }

                        Spacer(modifier = Modifier.height(16.dp))
                        Text(text = anuncio.nombreProfesional ?: "Profesional", fontSize = 24.sp, fontWeight = FontWeight.Bold)

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Star, null, tint = Color(0xFFFFB800), modifier = Modifier.size(16.dp))
                            val formatedRating = String.format("%.1f", anuncio.valoracionProfesional).replace(',', '.')
                            Text(" $formatedRating", fontWeight = FontWeight.Bold)
                            Text(" • ${anuncio.numeroValoracionesProfesional} reseñas", color = Color.Gray)
                        }

                        Spacer(modifier = Modifier.height(24.dp))

                        Surface(color = iMirlyPurple, shape = RoundedCornerShape(20.dp)) {
                            Column(modifier = Modifier.padding(horizontal = 32.dp, vertical = 12.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                                Text("${anuncio.precioHora}€", color = Color.White, fontSize = 28.sp, fontWeight = FontWeight.ExtraBold)
                                Text("por hora", color = Color.White.copy(alpha = 0.8f), fontSize = 12.sp)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(32.dp))

                    Text("Sobre mí", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(12.dp))
                    Surface(color = Color.White, shape = RoundedCornerShape(16.dp), modifier = Modifier.fillMaxWidth()) {
                        Text(text = anuncio.descripcion, modifier = Modifier.padding(20.dp), color = Color.DarkGray, lineHeight = 24.sp)
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.LocationOn, null, tint = iMirlyPurple, modifier = Modifier.size(18.dp))
                        Text("  ${anuncio.ubicacion}", color = Color.Gray, fontWeight = FontWeight.Medium)
                    }

                    Spacer(modifier = Modifier.height(100.dp))
                }
            }
        }
    }

    if (viewModel.showContactDialog.value) {
        AlertDialog(
            onDismissRequest = { viewModel.showContactDialog.value = false },
            containerColor = Color.White,
            title = { Text("Contactar Profesional", fontWeight = FontWeight.Bold) },
            text = {
                Column {
                    Text("Explica brevemente qué necesitas.", color = Color.Gray, fontSize = 14.sp)
                    Spacer(modifier = Modifier.height(16.dp))
                    OutlinedTextField(
                        value = viewModel.mensajeContacto.value,
                        onValueChange = { viewModel.mensajeContacto.value = it },
                        modifier = Modifier.fillMaxWidth().height(120.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = iMirlyPurple)
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.showContactDialog.value = false
                        viewModel.enviarSolicitud(anuncioId)
                    },
                    enabled = viewModel.mensajeContacto.value.isNotBlank() && !viewModel.isSubmitting.value,
                    colors = ButtonDefaults.buttonColors(containerColor = iMirlyPurple)
                ) {
                    Text("Enviar Solicitud")
                }
            },
            dismissButton = {
                TextButton(onClick = { viewModel.showContactDialog.value = false }) {
                    Text("Cancelar", color = Color.Gray)
                }
            }
        )
    }

    if (viewModel.requestSuccess.value) {
        AlertDialog(
            onDismissRequest = { 
                viewModel.requestSuccess.value = false 
                // Navegamos al inicio (Home) y limpiamos todo lo demás
                navController.navigate("home") {
                    popUpTo("home") { inclusive = false }
                }
            },
            containerColor = Color.White,
            icon = { Icon(Icons.Default.CheckCircle, null, tint = Color(0xFF2ECC71), modifier = Modifier.size(48.dp)) },
            title = { Text("¡Solicitud enviada!", textAlign = TextAlign.Center) },
            text = { Text("El profesional revisará tu solicitud. Podrás ver el estado en Mensajes.", textAlign = TextAlign.Center) },
            confirmButton = {
                Button(
                    onClick = { 
                        viewModel.requestSuccess.value = false 
                        // Volver al inicio real de la app (Home)
                        navController.navigate("home") {
                            popUpTo("home") { inclusive = false }
                        }
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