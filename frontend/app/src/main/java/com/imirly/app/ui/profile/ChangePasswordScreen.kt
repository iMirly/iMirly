package com.imirly.app.ui.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChangePasswordScreen(navController: NavController) {
    val viewModel: ChangePasswordViewModel = viewModel()
    val iMirlyPurple = Color(0xFF6C5CE7)
    val lightBackground = Color(0xFFFAFAFA)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Cambiar Contraseña", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(lightBackground)
                .padding(paddingValues)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Por tu seguridad, introduce tu contraseña actual para poder crear una nueva.",
                color = Color.Gray,
                fontSize = 14.sp
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Campo: Contraseña Actual
            PasswordTextField(
                value = viewModel.oldPassword.value,
                onValueChange = { viewModel.oldPassword.value = it },
                label = "Contraseña actual",
                iMirlyPurple = iMirlyPurple
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Campo: Nueva Contraseña
            PasswordTextField(
                value = viewModel.newPassword.value,
                onValueChange = { viewModel.newPassword.value = it },
                label = "Nueva contraseña",
                iMirlyPurple = iMirlyPurple
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Campo: Confirmar Nueva Contraseña
            PasswordTextField(
                value = viewModel.confirmPassword.value,
                onValueChange = { viewModel.confirmPassword.value = it },
                label = "Confirmar nueva contraseña",
                iMirlyPurple = iMirlyPurple
            )

            // Mensaje de Error / Éxito
            if (viewModel.mensaje.value.isNotEmpty()) {
                Spacer(modifier = Modifier.height(16.dp))
                val isSuccess = viewModel.mensaje.value.contains("éxito")
                Text(
                    text = viewModel.mensaje.value,
                    color = if (isSuccess) Color(0xFF2ECC71) else Color.Red,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            // Botón Guardar
            Button(
                onClick = {
                    viewModel.cambiarContrasena {
                        navController.popBackStack() // Si tiene éxito, volvemos al perfil
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = iMirlyPurple),
                enabled = !viewModel.isLoading.value
            ) {
                if (viewModel.isLoading.value) {
                    CircularProgressIndicator(modifier = Modifier.size(24.dp), color = Color.White, strokeWidth = 2.dp)
                } else {
                    Text("Actualizar contraseña", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PasswordTextField(value: String, onValueChange: (String) -> Unit, label: String, iMirlyPurple: Color) {
    var passwordVisible by remember { mutableStateOf(false) }
    
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
        trailingIcon = {
            val image = if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
            val description = if (passwordVisible) "Ocultar contraseña" else "Mostrar contraseña"

            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                Icon(imageVector = image, contentDescription = description)
            }
        },
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = iMirlyPurple),
        singleLine = true
    )
}