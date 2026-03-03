package com.imirly.app.ui.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.automirrored.outlined.List
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController

@Composable
fun ProfileScreen(rootNavController: NavController, viewModel: ProfileViewModel = viewModel()) {
    val iMirlyPurple = Color(0xFF6C5CE7)
    val lightBackground = Color(0xFFFAFAFA)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(lightBackground)
            .verticalScroll(rememberScrollState())
            .padding(24.dp)
    ) {
        // 1. Cabecera del Perfil
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier.size(64.dp).clip(CircleShape).background(iMirlyPurple),
                contentAlignment = Alignment.Center
            ) {
                // Saca las primeras 2 letras del nombre real para el avatar
                val iniciales = viewModel.userName.value.take(2).uppercase()
                Text(iniciales, color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.width(16.dp))

            // Textos
            Column {
                Text(viewModel.userName.value, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                Row(verticalAlignment = Alignment.CenterVertically) {
                    repeat(viewModel.valoracionMedia.value.toInt()) { Icon(Icons.Outlined.Star, contentDescription = null, tint = iMirlyPurple, modifier = Modifier.size(14.dp)) }
                    repeat(5 - viewModel.valoracionMedia.value.toInt()) { Icon(Icons.Outlined.Star, contentDescription = null, tint = Color.LightGray, modifier = Modifier.size(14.dp)) }
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(String.format("(%d)", viewModel.numeroValoraciones.value), fontSize = 12.sp, color = Color.Gray)
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // 2. Lista de Opciones
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = Color.White,
            shadowElevation = 2.dp
        ) {
            Column {
                ProfileMenuItem(icon = Icons.AutoMirrored.Outlined.List, title = "Mis anuncios") {
                    rootNavController.navigate("mis_anuncios")
                }
                Divider(color = Color(0xFFF0F0F0), thickness = 1.dp, modifier = Modifier.padding(horizontal = 16.dp))

                ProfileMenuItem(icon = Icons.Outlined.AccountBalanceWallet, title = "Saldo") {
                    rootNavController.navigate("saldo_screen")
                }
                Divider(color = Color(0xFFF0F0F0), thickness = 1.dp, modifier = Modifier.padding(horizontal = 16.dp))

                ProfileMenuItem(icon = Icons.Outlined.Security, title = "Datos personales") {
                    viewModel.prepararEdicion() // <--- ESTO RELLENA LOS DATOS ANTES DE ABRIR
                }
                Divider(color = Color(0xFFF0F0F0), thickness = 1.dp, modifier = Modifier.padding(horizontal = 16.dp))

                ProfileMenuItem(icon = Icons.Outlined.Lock, title = "Cambiar contraseña") {
                    rootNavController.navigate("change_password")
                }
                Divider(color = Color(0xFFF0F0F0), thickness = 1.dp, modifier = Modifier.padding(horizontal = 16.dp))

                ProfileMenuItem(icon = Icons.Outlined.ChatBubbleOutline, title = "Chat de Mirly") { 
                    rootNavController.navigate("mirly_chat")
                }
                Divider(color = Color(0xFFF0F0F0), thickness = 1.dp, modifier = Modifier.padding(horizontal = 16.dp))

                ProfileMenuItem(icon = Icons.Outlined.Info, title = "Sobre Mirly") { 
                    rootNavController.navigate("about_mirly")
                }
                Divider(color = Color(0xFFF0F0F0), thickness = 1.dp, modifier = Modifier.padding(horizontal = 16.dp))

                ProfileMenuItem(icon = Icons.Outlined.HelpOutline, title = "Ayuda") { 
                    rootNavController.navigate("help_center")
                }
                Divider(color = Color(0xFFF0F0F0), thickness = 1.dp, modifier = Modifier.padding(horizontal = 16.dp))

                ProfileMenuItem(icon = Icons.Outlined.Email, title = "Contacta con nosotros") { 
                    rootNavController.navigate("contact_us")
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // 3. Botón de Cerrar Sesión (Destacado en rojo)
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = Color.White,
            shadowElevation = 2.dp,
            modifier = Modifier.clickable {
                viewModel.showLogoutDialog.value = true // <--- AHORA ABRE EL DIÁLOGO
            }
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.AutoMirrored.Filled.ExitToApp, contentDescription = null, tint = Color(0xFFFF4757))
                Spacer(modifier = Modifier.width(16.dp))
                Text("Cerrar sesión", fontSize = 16.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFFFF4757))
                Spacer(modifier = Modifier.weight(1f))
                Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = null, tint = Color(0xFFFF4757))
            }
        }

        Spacer(modifier = Modifier.height(40.dp)) // Espacio para la barra de navegación
    }

    if (viewModel.showEditDialog.value) {
        EditProfileDialog(viewModel, rootNavController)
    }

    // DIÁLOGO DE CONFIRMACIÓN DE CIERRE DE SESIÓN
    if (viewModel.showLogoutDialog.value) {
        AlertDialog(
            onDismissRequest = { viewModel.showLogoutDialog.value = false },
            title = { Text("Cerrar Sesión", fontWeight = FontWeight.Bold) },
            text = { Text("¿Estás seguro de que quieres salir de tu cuenta?") },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.showLogoutDialog.value = false
                    viewModel.cerrarSesion {
                        rootNavController.navigate("login") {
                            popUpTo(0) { inclusive = true }
                        }
                    }
                }) {
                    Text("Sí, salir", color = Color(0xFFFF4757), fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { viewModel.showLogoutDialog.value = false }) {
                    Text("Cancelar", color = Color.Gray)
                }
            }
        )
    }
}

// Componente reutilizable para cada fila del menú
@Composable
fun ProfileMenuItem(icon: ImageVector, title: String, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, contentDescription = null, tint = Color(0xFF6C5CE7))
        Spacer(modifier = Modifier.width(16.dp))
        Text(title, fontSize = 16.sp, fontWeight = FontWeight.Medium, color = Color.DarkGray)
        Spacer(modifier = Modifier.weight(1f))
        Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = null, tint = Color.LightGray)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileDialog(viewModel: ProfileViewModel, navController: NavController) { // <-- Pasamos el navController
    val iMirlyPurple = Color(0xFF6C5CE7)

    AlertDialog(
        onDismissRequest = { viewModel.showEditDialog.value = false },
        containerColor = Color.White,
        title = {
            Text(text = "Editar Datos Personales", fontWeight = FontWeight.Bold)
        },
        text = {
            Column {
                Text("Actualiza tu información pública.", color = Color.Gray, fontSize = 14.sp)
                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = viewModel.editNombre.value,
                    onValueChange = { viewModel.editNombre.value = it },
                    label = { Text("Nombre") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = iMirlyPurple)
                )
                Spacer(modifier = Modifier.height(12.dp))

                // 👇 NUEVO CAMPO DE CORREO 👇
                OutlinedTextField(
                    value = viewModel.editEmail.value,
                    onValueChange = { viewModel.editEmail.value = it },
                    label = { Text("Correo Electrónico") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = iMirlyPurple)
                )
                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = viewModel.editDni.value,
                    onValueChange = { viewModel.editDni.value = it },
                    label = { Text("Documento de Identidad (DNI/NIE)") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = iMirlyPurple)
                )

                if (viewModel.mensaje.value.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = viewModel.mensaje.value,
                        // 👇 SOLUCIÓN 3: CAMBIAMOS "éxito" POR "actualizado" PARA QUE SEA VERDE 👇
                        color = if (viewModel.mensaje.value.contains("actualizado")) Color(0xFF2ECC71) else Color.Red,
                        fontSize = 12.sp
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    viewModel.actualizarPerfil {
                        navController.navigate("login") {
                            popUpTo(0) { inclusive = true }
                        }
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = iMirlyPurple),
                // 👇 SOLUCIÓN 2 (UX): EL BOTÓN SE APAGA SI ESTÁN VACÍOS 👇
                enabled = !viewModel.isLoading.value && viewModel.editNombre.value.isNotBlank() && viewModel.editEmail.value.isNotBlank()
            ) {
                Text(if (viewModel.isLoading.value) "Guardando..." else "Guardar")
            }
        },
        dismissButton = {
            TextButton(onClick = { viewModel.showEditDialog.value = false }) {
                Text("Cancelar", color = Color.Gray)
            }
        }
    )
}