package com.imirly.app.ui.chat

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.imirly.app.network.MensajeResponse
import java.text.SimpleDateFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(
    navController: NavController,
    otroUsuarioId: String,
    nombreContacto: String,
    solicitudId: String,
    soyElProveedor: Boolean,
    viewModel: ChatViewModel = viewModel()
) {
    val iMirlyPurple = Color(0xFF6C5CE7)
    val bgChat = Color(0xFFF5F5F5)

    // Estado único para el popup de confirmación
    var mostrarConfirmacionBorrar by remember { mutableStateOf(false) }
    val context = LocalContext.current

    DisposableEffect(otroUsuarioId) {
        viewModel.startPolling(otroUsuarioId)
        onDispose { viewModel.stopPolling() }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(shape = CircleShape, color = Color(0xFFE0E0E0), modifier = Modifier.size(40.dp)) {
                            Icon(Icons.Default.Person, contentDescription = null, tint = Color.Gray, modifier = Modifier.padding(8.dp))
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(nombreContacto, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    }
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                },
                actions = {
                    // Papelera roja directa en la barra superior
                    IconButton(onClick = { mostrarConfirmacionBorrar = true }) {
                        Icon(Icons.Default.Delete, contentDescription = "Borrar chat", tint = Color.Red)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        },
        bottomBar = { 
            val isCompleted = viewModel.mensajes.value.any { it.tipo == "SERVICIO_COMPLETADO" }
            val hasRated = viewModel.mensajes.value.any { it.tipo == "VALORACION" && it.remitenteId != otroUsuarioId }
            
            if (isCompleted && !hasRated) {
                ValoracionBar(viewModel, otroUsuarioId, solicitudId, iMirlyPurple)
            } else {
                ChatInputBar(viewModel, otroUsuarioId, solicitudId, soyElProveedor, iMirlyPurple) 
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().background(bgChat).padding(paddingValues).padding(horizontal = 16.dp),
            reverseLayout = true
        ) {
            val mensajesReversed = viewModel.mensajes.value.reversed()

            // Usamos itemsIndexed para saber en qué posición estamos
            itemsIndexed(mensajesReversed) { index, mensaje ->
                val isMine = mensaje.remitenteId != otroUsuarioId

                // MAGIA: Buscamos si hay respuestas más recientes en el chat
                val mensajesMasNuevos = mensajesReversed.subList(0, index)
                val yaRespondido = mensajesMasNuevos.any {
                    it.tipo != null && it.tipo != "TEXTO"
                }

                ChatBubble(
                    mensaje = mensaje,
                    isMine = isMine,
                    primaryColor = iMirlyPurple,
                    yaRespondido = yaRespondido, 
                    soyElProveedor = soyElProveedor,
                    onPagarClick = { viewModel.aceptarYPagar(solicitudId, otroUsuarioId) },
                    onRechazarClick = { viewModel.rechazarPresupuesto(solicitudId, otroUsuarioId) },
                    onFinalizarTrabajoClick = { viewModel.marcarTrabajoFinalizado(otroUsuarioId, solicitudId) },
                    onAprobarTrabajoClick = { viewModel.aprobarTrabajo(otroUsuarioId, solicitudId) },
                    onRechazarTrabajoClick = { viewModel.rechazarTrabajo(otroUsuarioId, solicitudId) }
                )
            }
        }
    }

    // ---------------- POPUP DE CONFIRMACIÓN ----------------
    if (mostrarConfirmacionBorrar) {
        AlertDialog(
            onDismissRequest = { mostrarConfirmacionBorrar = false },
            title = { Text("Borrar chat") },
            text = { Text("¿Estás seguro de que deseas borrar este chat? Se eliminarán todos los mensajes y la solicitud.") },
            confirmButton = {
                TextButton(onClick = {
                    // Llamamos a la nueva función con control de errores
                    viewModel.borrarChatYSolicitud(
                        otroUsuarioId = otroUsuarioId,
                        solicitudId = solicitudId,
                        onSuccess = { navController.popBackStack() },
                        onError = { mensajeError ->
                            Toast.makeText(context, mensajeError, Toast.LENGTH_LONG).show()
                        }
                    )
                    mostrarConfirmacionBorrar = false
                }) { Text("Borrar", color = Color.Red) }
            },
            dismissButton = {
                TextButton(onClick = { mostrarConfirmacionBorrar = false }) { Text("Cancelar", color = Color.Gray) }
            }
        )
    }
}

@Composable
fun ChatBubble(
    mensaje: MensajeResponse,
    isMine: Boolean,
    primaryColor: Color,
    yaRespondido: Boolean = false,
    soyElProveedor: Boolean,
    onPagarClick: () -> Unit,
    onRechazarClick: () -> Unit,
    onFinalizarTrabajoClick: () -> Unit,
    onAprobarTrabajoClick: () -> Unit,
    onRechazarTrabajoClick: () -> Unit
) {
    val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
    val horaStr = try {
        val date = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault()).parse(mensaje.timestamp)
        timeFormat.format(date!!)
    } catch (e: Exception) { "" }

    // 👇 MAGIA: Cualquier mensaje del ecosistema de presupuesto se vuelve amarillo 👇
    val isPresupuestoMsg = mensaje.tipo != null && mensaje.tipo != "TEXTO"
    val bgColor = if (isPresupuestoMsg) Color(0xFFFFF9E6) else if (isMine) primaryColor else Color.White
    val textColor = if (isPresupuestoMsg) Color.Black else if (isMine) Color.White else Color.Black
    val shadow = if (isPresupuestoMsg) 4.dp else 1.dp

    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
        horizontalArrangement = if (isMine) Arrangement.End else Arrangement.Start
    ) {
        Surface(
            color = bgColor,
            shape = RoundedCornerShape(
                topStart = 16.dp, topEnd = 16.dp,
                bottomStart = if (isMine) 16.dp else 4.dp,
                bottomEnd = if (isMine) 4.dp else 16.dp
            ),
            shadowElevation = shadow,
            modifier = Modifier.widthIn(max = 280.dp)
        ) {
            Column(modifier = Modifier.padding(12.dp)) {

                if (mensaje.tipo == "PROPUESTA_PRECIO") {
                    // 👇 Separamos el precio y la descripción usando el "pipe" 👇
                    val partes = mensaje.contenido.split("|")
                    val precio = partes.getOrNull(0) ?: mensaje.contenido
                    val descripcion = partes.getOrNull(1) ?: ""

                    Text("Presupuesto Acordado", fontWeight = FontWeight.Bold, color = Color(0xFFFFB800), fontSize = 12.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("$precio €", fontSize = 24.sp, fontWeight = FontWeight.ExtraBold, color = Color.Black)

                    if (descripcion.isNotBlank()) {
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(descripcion, fontSize = 14.sp, color = Color.DarkGray)
                    }
                    Spacer(modifier = Modifier.height(8.dp))

                    if (!isMine) {
                        if (yaRespondido) {
                            Text("Oferta respondida", color = Color.Gray, fontSize = 12.sp, fontStyle = androidx.compose.ui.text.font.FontStyle.Italic)
                        } else {
                            Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                Button(onClick = onPagarClick, colors = ButtonDefaults.buttonColors(containerColor = primaryColor), modifier = Modifier.fillMaxWidth()) {
                                    Text("Aceptar y Pagar")
                                }
                                OutlinedButton(onClick = onRechazarClick, colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Red), modifier = Modifier.fillMaxWidth()) {
                                    Text("Rechazar Oferta")
                                }
                            }
                        }
                    } else {
                        Text("Esperando pago del cliente...", color = Color.Gray, fontSize = 12.sp)
                    }
                }
                // 👇 NUEVO: Diseño de las respuestas del presupuesto 👇
                else if (mensaje.tipo == "PRESUPUESTO_ACEPTADO") {
                    Text("Presupuesto Aceptado", fontWeight = FontWeight.Bold, color = Color(0xFFFFB800), fontSize = 12.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(mensaje.contenido, color = Color.Black, fontSize = 15.sp)
                    
                    if (soyElProveedor && !yaRespondido) {
                        Spacer(modifier = Modifier.height(12.dp))
                        Button(onClick = onFinalizarTrabajoClick, colors = ButtonDefaults.buttonColors(containerColor = primaryColor), modifier = Modifier.fillMaxWidth()) {
                            Text("Marcar Trabajo como Finalizado")
                        }
                    }
                }
                else if (mensaje.tipo == "PRESUPUESTO_RECHAZADO") {
                    Text("Presupuesto Rechazado", fontWeight = FontWeight.Bold, color = Color.Red, fontSize = 12.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(mensaje.contenido, color = Color.Black, fontSize = 15.sp)
                }
                else if (mensaje.tipo == "TRABAJO_FINALIZADO") {
                    Text("Trabajo Finalizado", fontWeight = FontWeight.Bold, color = Color(0xFF2ECC71), fontSize = 12.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(mensaje.contenido, color = Color.Black, fontSize = 15.sp)

                    if (!soyElProveedor && !yaRespondido) {
                        Spacer(modifier = Modifier.height(12.dp))
                        Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Button(onClick = onAprobarTrabajoClick, colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2ECC71)), modifier = Modifier.fillMaxWidth()) {
                                Text("Aprobar y Liberar Pago")
                            }
                            OutlinedButton(onClick = onRechazarTrabajoClick, colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Red), modifier = Modifier.fillMaxWidth()) {
                                Text("Rechazar (Exigir Arreglos)")
                            }
                        }
                    }
                }
                else if (mensaje.tipo == "SERVICIO_COMPLETADO") {
                    Text("Servicio Completado", fontWeight = FontWeight.Bold, color = Color(0xFF2ECC71), fontSize = 12.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(mensaje.contenido, color = Color.Black, fontSize = 15.sp)
                }
                else if (mensaje.tipo == "SERVICIO_RECHAZADO") {
                    Text("Trabajo Rechazado", fontWeight = FontWeight.Bold, color = Color.Red, fontSize = 12.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(mensaje.contenido, color = Color.Black, fontSize = 15.sp)
                    
                    if (soyElProveedor && !yaRespondido) {
                        Spacer(modifier = Modifier.height(12.dp))
                        Button(onClick = onFinalizarTrabajoClick, colors = ButtonDefaults.buttonColors(containerColor = primaryColor), modifier = Modifier.fillMaxWidth()) {
                            Text("Volver a Marcar como Finalizado")
                        }
                    }
                }
                else if (mensaje.tipo == "VALORACION") {
                    val partes = mensaje.contenido.split("|")
                    val nota = partes.getOrNull(0)?.toDoubleOrNull() ?: 5.0
                    Text("Valoración del Servicio", fontWeight = FontWeight.Bold, color = Color(0xFFFFB800), fontSize = 12.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        repeat(nota.toInt()) { Icon(Icons.Filled.Star, contentDescription = null, tint = Color(0xFFFFB800), modifier = Modifier.size(16.dp)) }
                        repeat(5 - nota.toInt()) { Icon(Icons.Outlined.Star, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(16.dp)) }
                    }
                }
                else {
                    Text(text = mensaje.contenido, color = textColor, fontSize = 15.sp)
                }

                Spacer(modifier = Modifier.height(4.dp))
                Row(modifier = Modifier.align(Alignment.End), verticalAlignment = Alignment.CenterVertically) {
                    Text(text = horaStr, color = if (isPresupuestoMsg) Color.Gray else if (isMine) Color.White.copy(alpha = 0.7f) else Color.Gray, fontSize = 10.sp)
                    if (isMine) {
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(if (mensaje.leido) "✓✓" else "✓", color = if (isPresupuestoMsg) Color.Gray else if (mensaje.leido) Color(0xFF4FC3F7) else Color.White.copy(alpha = 0.7f), fontSize = 10.sp)
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatInputBar(viewModel: ChatViewModel, otroUsuarioId: String, solicitudId: String, soyElProveedor: Boolean, primaryColor: Color) {
    // Estado para el mini-diálogo de poner el precio
    var showDialogPrecio by remember { mutableStateOf(false) }
    var precioTemp by remember { mutableStateOf("") }
    var descTemp by remember { mutableStateOf("") }

    Surface(color = Color.White, shadowElevation = 8.dp) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (soyElProveedor) {
                IconButton(onClick = { showDialogPrecio = true }) {
                    Icon(Icons.Default.Assignment, contentDescription = "Hacer Oferta", tint = primaryColor)
                }
            }

            OutlinedTextField(
                value = viewModel.inputText.value,
                onValueChange = { viewModel.inputText.value = it },
                placeholder = { Text("Escribe un mensaje...", color = Color.Gray) },
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(24.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color.Transparent,
                    unfocusedBorderColor = Color.Transparent,
                    focusedContainerColor = Color(0xFFF5F5F5),
                    unfocusedContainerColor = Color(0xFFF5F5F5)
                ),
                maxLines = 4
            )
            Spacer(modifier = Modifier.width(8.dp))
            FloatingActionButton(
                onClick = { viewModel.enviarMensaje(otroUsuarioId) },
                containerColor = primaryColor,
                contentColor = Color.White,
                shape = CircleShape,
                modifier = Modifier.size(48.dp)
            ) {
                Icon(Icons.Default.Send, contentDescription = "Enviar", modifier = Modifier.padding(start = 4.dp))
            }
        }
    }

    // 👇 DIÁLOGO PARA PONER EL PRECIO 👇
    if (showDialogPrecio) {
        AlertDialog(
            onDismissRequest = { showDialogPrecio = false },
            title = { Text("Enviar Presupuesto") },
            text = {
                Column {
                    OutlinedTextField(
                        value = precioTemp,
                        onValueChange = { precioTemp = it },
                        label = { Text("Precio total (€)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = descTemp,
                        onValueChange = { descTemp = it },
                        label = { Text("Descripción (opcional)") },
                        maxLines = 3
                    )
                }
            },
            confirmButton = {
                Button(onClick = {
                    val finalString = "$precioTemp|$descTemp"
                    viewModel.enviarPresupuesto(otroUsuarioId, solicitudId, finalString)
                    showDialogPrecio = false
                    precioTemp = ""
                    descTemp = ""
                }) { Text("Enviar") }
            },
            dismissButton = {
                TextButton(onClick = { showDialogPrecio = false }) { Text("Cancelar") }
            }
        )
    }
}

@Composable
fun ValoracionBar(viewModel: ChatViewModel, otroUsuarioId: String, solicitudId: String, primaryColor: Color) {
    var selectedStar by remember { mutableStateOf(0) }

    Surface(color = Color(0xFFFFF9E6), shadowElevation = 8.dp) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("¡El servicio ha finalizado!", fontWeight = FontWeight.Bold, color = primaryColor)
            Text("Valora tu experiencia de 1 a 5 estrellas.", fontSize = 14.sp, color = Color.DarkGray)
            
            Spacer(modifier = Modifier.height(12.dp))
            
            Row(horizontalArrangement = Arrangement.Center) {
                for (i in 1..5) {
                    IconButton(onClick = { selectedStar = i }) {
                        if (i <= selectedStar) {
                            Icon(Icons.Filled.Star, contentDescription = null, tint = Color(0xFFFFB800), modifier = Modifier.size(36.dp))
                        } else {
                            Icon(Icons.Outlined.Star, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(36.dp))
                        }
                    }
                }
            }
            
            if (selectedStar > 0) {
                Spacer(modifier = Modifier.height(8.dp))
                Button(
                    onClick = { viewModel.enviarValoracion(otroUsuarioId, solicitudId, selectedStar.toDouble()) },
                    colors = ButtonDefaults.buttonColors(containerColor = primaryColor),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Enviar Valoración")
                }
            }
        }
    }
}