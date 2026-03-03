package com.imirly.app.ui.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController


@Composable
fun LoginScreen(navController: NavController, viewModel: LoginViewModel = viewModel()) {
    val iMirlyPurple = Color(0xFF6C5CE7)
    Box(modifier = Modifier.fillMaxSize().background(iMirlyPurple).padding(24.dp), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("iMirly", color = Color.White, fontSize = 42.sp, fontWeight = FontWeight.ExtraBold)
            Spacer(modifier = Modifier.height(32.dp))
            Surface(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(28.dp), color = Color.White) {
                Column(modifier = Modifier.padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Iniciar sesión", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(24.dp))
                    CustomTextField(
                        value = viewModel.email.value,
                        onValueChange = { viewModel.email.value = it },
                        label = "Email",
                        icon = Icons.Default.Email,
                        containerColor = Color(0xFFF8F9FA)
                    )
                    CustomTextField(
                        value = viewModel.password.value,
                        onValueChange = { viewModel.password.value = it },
                        label = "Contraseña",
                        icon = Icons.Default.Lock,
                        isPassword = true,
                        containerColor = Color(0xFFF8F9FA)
                    )

                    if (viewModel.mensaje.value.isNotEmpty()) {
                        Text(
                            text = viewModel.mensaje.value,
                            color = if (viewModel.mensaje.value.contains("éxito")) Color(0xFF2ECC71) else Color.Red, // Verde para éxito
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier.padding(top = 10.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(24.dp))
                    Button(
                        onClick = {
                            viewModel.iniciarSesion {
                                // Navegamos a "main" y destruimos las pantallas anteriores
                                // para que al pulsar "Atrás" en el móvil no volvamos al Login
                                navController.navigate("main") {
                                    popUpTo("login") { inclusive = true }
                                }
                            }
                        },
                        modifier = Modifier.fillMaxWidth().height(50.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = iMirlyPurple)
                    ) {
                        Text(if (viewModel.isLoading.value) "Cargando..." else "Iniciar sesión")
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("¿No tienes cuenta? Crear cuenta", color = iMirlyPurple, fontWeight = FontWeight.SemiBold, modifier = Modifier.clickable { navController.navigate("register") })
                }
            }
        }
    }
}