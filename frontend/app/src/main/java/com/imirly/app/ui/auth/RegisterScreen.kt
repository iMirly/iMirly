package com.imirly.app.ui.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.imirly.app.ui.auth.RegisterViewModel

@Composable
fun RegisterScreen(navController: NavController, viewModel: RegisterViewModel = viewModel()) {
    val iMirlyPurple = Color(0xFF6C5CE7)
    val fieldBackground = Color(0xFFF8F9FA)

    LaunchedEffect(viewModel.mensaje.value) {
        if (viewModel.mensaje.value.contains("éxito")) {
            kotlinx.coroutines.delay(1000) // Small delay to show the success message
            navController.navigate("login") {
                popUpTo("register") { inclusive = true }
            }
        }
    }

    // El Box ahora alinea TODO al centro
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(iMirlyPurple)
            .padding(24.dp), // Margen externo para que no toque los bordes del móvil
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState()) // Por si el teclado tapa algo en pantallas pequeñas
        ) {
            // Logo "iMirly"
            Text(
                text = "iMirly",
                color = Color.White,
                fontSize = 42.sp,
                fontWeight = FontWeight.ExtraBold
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Tarjeta Blanca Centrada
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(28.dp), // Redondeado en todas las esquinas
                color = Color.White,
                shadowElevation = 8.dp // Un poco de sombra para que resalte
            ) {
                Column(
                    modifier = Modifier
                        .padding(24.dp)
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Crear cuenta",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    CustomTextField(
                        value = viewModel.nombre.value,
                        onValueChange = { viewModel.nombre.value = it },
                        label = "Nombre",
                        icon = Icons.Default.Person,
                        containerColor = fieldBackground
                    )
                    CustomTextField(
                        value = viewModel.email.value,
                        onValueChange = { viewModel.email.value = it },
                        label = "E-mail",
                        icon = Icons.Default.Email,
                        containerColor = fieldBackground
                    )
                    CustomTextField(
                        value = viewModel.password.value,
                        onValueChange = { viewModel.password.value = it },
                        label = "Contraseña",
                        icon = Icons.Default.Lock,
                        isPassword = true,
                        containerColor = fieldBackground
                    )
                    CustomTextField(
                        value = viewModel.confirmPassword.value,
                        onValueChange = { viewModel.confirmPassword.value = it },
                        label = "Confirmar contraseña",
                        icon = Icons.Default.Lock,
                        isPassword = true,
                        containerColor = fieldBackground
                    )

                    if (viewModel.mensaje.value.isNotEmpty()) {
                        Text(
                            text = viewModel.mensaje.value,
                            color = if (viewModel.mensaje.value.contains("éxito")) Color(0xFF2ECC71) else Color.Red,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier.padding(top = 10.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    Button(
                        onClick = { viewModel.registrarUsuario() },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = iMirlyPurple),
                        enabled = !viewModel.isLoading.value
                    ) {
                        Text(if (viewModel.isLoading.value) "Cargando..." else "Crear cuenta")
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "¿Ya tienes cuenta? Iniciar sesión",
                        color = iMirlyPurple,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.clickable {
                            navController.navigate("login") // <--- Ahora ya funciona!
                        }
                    )
                }
            }
        }
    }
}

// El componente CustomTextField se mantiene igual que antes
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    icon: ImageVector,
    containerColor: Color,
    isPassword: Boolean = false
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label, fontSize = 14.sp) },
        leadingIcon = { Icon(icon, contentDescription = null, modifier = Modifier.size(20.dp)) },
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        shape = RoundedCornerShape(12.dp),
        singleLine = true,
        visualTransformation = if (isPassword) PasswordVisualTransformation() else VisualTransformation.None,
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = containerColor,
            unfocusedContainerColor = containerColor,
            focusedBorderColor = Color(0xFF6C5CE7),
            unfocusedBorderColor = Color.Transparent,
            focusedLabelColor = Color(0xFF6C5CE7)
        )
    )
}