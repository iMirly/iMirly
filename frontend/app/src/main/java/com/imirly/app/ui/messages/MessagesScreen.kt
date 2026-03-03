package com.imirly.app.ui.messages

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
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
import com.imirly.app.network.SolicitudResponse
import androidx.navigation.NavController // Asegúrate de tener esta importación arriba

@Composable
fun MessagesScreen(navController: NavController, viewModel: MessagesViewModel = viewModel()) {
    val iMirlyPurple = Color(0xFF6C5CE7)

    // 👇 NUEVO: Forzamos la actualización cada vez que la pantalla se hace visible 👇
    LaunchedEffect(Unit) {
        viewModel.cargarSolicitudes()
    }

    Column(modifier = Modifier.fillMaxSize().background(Color(0xFFFAFAFA))) {
        // Cabecera
        Text(
            text = "Bandeja de Entrada",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(24.dp)
        )

        if (viewModel.isLoading.value) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = iMirlyPurple)
            }
        } else if (viewModel.solicitudes.value.isEmpty()) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("No tienes solicitudes nuevas", color = Color.Gray)
            }
        } else {
            LazyColumn(
                contentPadding = PaddingValues(horizontal = 24.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(viewModel.solicitudes.value) { solicitud ->
                    // 👇 Añadimos el navController aquí 👇
                    SolicitudCard(solicitud, viewModel, navController)
                }
            }
        }
    }
}

@Composable
fun SolicitudCard(solicitud: SolicitudResponse, viewModel: MessagesViewModel, navController: NavController) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = Color.White,
        shadowElevation = 2.dp,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Surface(shape = RoundedCornerShape(20.dp), color = Color(0xFFF0F0F0), modifier = Modifier.size(40.dp)) {
                    Icon(Icons.Default.Person, null, tint = Color.Gray, modifier = Modifier.padding(8.dp))
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    // 👇 ARREGLO 1: Pintamos el nombre del "otro", no el mío 👇
                    val nombreMostrar = if (solicitud.soyElProveedor) solicitud.nombreCliente else solicitud.nombreProveedor
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = nombreMostrar ?: "Usuario",
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            maxLines = 1,
                            overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis,
                            modifier = Modifier.weight(1f)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        val tagText = if (solicitud.soyElProveedor) "CLIENTE" else "PROVEEDOR"
                        Surface(
                            shape = RoundedCornerShape(4.dp),
                            color = Color(0xFF6C5CE7).copy(alpha = 0.1f)
                        ) {
                            Text(
                                text = tagText,
                                color = Color(0xFF6C5CE7),
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }

                    // Mejoramos la visualización del estado
                    Text("Estado: ${solicitud.estado.replace("_", " ")}", color = Color.Gray, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Surface(color = Color(0xFFFAFAFA), shape = RoundedCornerShape(8.dp), modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "\"${solicitud.detallesSolicitud}\"",
                    modifier = Modifier.padding(12.dp),
                    color = Color.DarkGray,
                    fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
                    fontSize = 14.sp
                )
            }

            if (solicitud.estado == "PENDIENTE") {
                Spacer(modifier = Modifier.height(16.dp))
                if (solicitud.soyElProveedor) {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        OutlinedButton(
                            onClick = { viewModel.cambiarEstado(solicitud.id, "RECHAZADO") },
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFFFF4757))
                        ) { Text("Rechazar") }
                        Button(
                            onClick = { viewModel.cambiarEstado(solicitud.id, "ACEPTADO") },
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2ECC71))
                        ) { Text("Aceptar") }
                    }
                } else {
                    Text(
                        text = "Esperando a que el profesional acepte la solicitud...",
                        color = Color.Gray,
                        fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
                        fontSize = 13.sp,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }

            }
            // 👇 ARREGLO 2: Si NO está PENDIENTE ni RECHAZADO, siempre pintamos el botón del chat 👇
            else if (solicitud.estado != "RECHAZADO") {
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = {
                        val otroUsuarioId = if (solicitud.soyElProveedor) solicitud.clienteId else solicitud.proveedorId ?: ""
                        val nombreChat = if (solicitud.soyElProveedor) solicitud.nombreCliente else solicitud.nombreProveedor
                        val nombreSeguro = android.net.Uri.encode(nombreChat ?: "Usuario")

                        // Donde tienes el navController.navigate(...) cámbialo por esto:
                        val soyElProvStr = solicitud.soyElProveedor.toString()
                        navController.navigate("chat/${otroUsuarioId}/$nombreSeguro/${solicitud.id}/$soyElProvStr")
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6C5CE7))
                ) {
                    Text(if (solicitud.estado == "PENDIENTE_PAGO") "Ver Presupuesto" else "Abrir Chat")
                }
            }
        }
    }
}