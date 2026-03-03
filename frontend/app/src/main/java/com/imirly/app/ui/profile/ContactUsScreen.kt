package com.imirly.app.ui.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Phone
import androidx.compose.material.icons.outlined.ChatBubbleOutline
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContactUsScreen(navController: NavController) {
    val lightBackground = Color(0xFFFAFAFA)
    val iMirlyPurple = Color(0xFF6C5CE7)
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Contacta con nosotros", fontWeight = FontWeight.Bold, fontSize = 18.sp) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) { // TODO: Go back
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White,
                    titleContentColor = Color.Black,
                    navigationIconContentColor = Color.Black
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(lightBackground)
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            // Métodos de contacto
            Text("Métodos de contacto", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = Color.Black)
            Spacer(modifier = Modifier.height(16.dp))
            
            ContactMethodCard(
                icon = Icons.Outlined.Email,
                iconColor = Color(0xFF1E88E5),
                iconBgColor = Color(0xFFE3F2FD),
                title = "Email",
                subtitle = "soporte@imirly.com",
                subtitleColor = iMirlyPurple,
                desc = "Respuesta en 24-48 horas"
            )
            Spacer(modifier = Modifier.height(12.dp))
            ContactMethodCard(
                icon = Icons.Outlined.ChatBubbleOutline,
                iconColor = Color(0xFF43A047),
                iconBgColor = Color(0xFFE8F5E9),
                title = "Chat en vivo",
                subtitle = "Disponible",
                subtitleColor = iMirlyPurple,
                desc = "Lun-Vie 9:00-18:00"
            )
            Spacer(modifier = Modifier.height(12.dp))
            ContactMethodCard(
                icon = Icons.Outlined.Phone,
                iconColor = Color(0xFF9C27B0),
                iconBgColor = Color(0xFFF3E5F5),
                title = "Teléfono",
                subtitle = "+34 900 123 456",
                subtitleColor = iMirlyPurple,
                desc = "Lun-Vie 9:00-18:00"
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Envíanos un mensaje
            Text("Envíanos un mensaje", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = Color.Black)
            Spacer(modifier = Modifier.height(16.dp))

            Surface(
                shape = RoundedCornerShape(16.dp),
                color = Color.White,
                shadowElevation = 2.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    // Form fields
                    Text("Motivo de contacto", fontSize = 12.sp, color = Color.Gray, modifier = Modifier.padding(start = 4.dp))
                    Spacer(modifier = Modifier.height(4.dp))
                    OutlinedTextField(
                        value = "",
                        onValueChange = {},
                        enabled = false, // Looking like a dropdown
                        placeholder = { Text("Soporte técnico") },
                        modifier = Modifier.fillMaxWidth().clickable { /* TODO: Dropdown */ },
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            disabledBorderColor = Color(0xFFE0E0E0),
                            disabledTextColor = Color.Black,
                            disabledPlaceholderColor = Color.Black
                        )
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    Text("Nombre completo", fontSize = 12.sp, color = Color.Gray, modifier = Modifier.padding(start = 4.dp))
                    Spacer(modifier = Modifier.height(4.dp))
                    OutlinedTextField(
                        value = "",
                        onValueChange = {},
                        placeholder = { Text("Tu nombre", color = Color.LightGray) },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            unfocusedBorderColor = Color(0xFFE0E0E0),
                            focusedBorderColor = iMirlyPurple
                        )
                    )

                    Spacer(modifier = Modifier.height(16.dp))
                    
                    Text("Email", fontSize = 12.sp, color = Color.Gray, modifier = Modifier.padding(start = 4.dp))
                    Spacer(modifier = Modifier.height(4.dp))
                    OutlinedTextField(
                        value = "",
                        onValueChange = {},
                        placeholder = { Text("tu@email.com", color = Color.LightGray) },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            unfocusedBorderColor = Color(0xFFE0E0E0),
                            focusedBorderColor = iMirlyPurple
                        )
                    )

                    Spacer(modifier = Modifier.height(16.dp))
                    
                    Text("Asunto", fontSize = 12.sp, color = Color.Gray, modifier = Modifier.padding(start = 4.dp))
                    Spacer(modifier = Modifier.height(4.dp))
                    OutlinedTextField(
                        value = "",
                        onValueChange = {},
                        placeholder = { Text("Título del mensaje", color = Color.LightGray) },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            unfocusedBorderColor = Color(0xFFE0E0E0),
                            focusedBorderColor = iMirlyPurple
                        )
                    )

                    Spacer(modifier = Modifier.height(16.dp))
                    
                    Text("Mensaje", fontSize = 12.sp, color = Color.Gray, modifier = Modifier.padding(start = 4.dp))
                    Spacer(modifier = Modifier.height(4.dp))
                    OutlinedTextField(
                        value = "",
                        onValueChange = {},
                        placeholder = { Text("Describe tu consulta...", color = Color.LightGray) },
                        modifier = Modifier.fillMaxWidth().height(120.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            unfocusedBorderColor = Color(0xFFE0E0E0),
                            focusedBorderColor = iMirlyPurple
                        ),
                        maxLines = 5
                    )
                    
                    Spacer(modifier = Modifier.height(24.dp))
                    
                    Button(
                        onClick = { /* TODO: Send */ },
                        modifier = Modifier.fillMaxWidth().height(50.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFD0D3D4), // Gray color from the UI indicating it's disabled / not filled
                            contentColor = Color.White
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(Icons.AutoMirrored.Filled.Send, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Enviar mensaje", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    }
                    
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Text(
                "Nota:\nNuestro equipo de soporte responderá tu mensaje lo antes posible.\nEl tiempo de respuesta habitual es de 24-48 horas.",
                color = iMirlyPurple,
                fontSize = 11.sp,
                modifier = Modifier.fillMaxWidth().padding(horizontal = 4.dp),
                lineHeight = 16.sp
            )
            
            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}

@Composable
fun ContactMethodCard(icon: ImageVector, iconColor: Color, iconBgColor: Color, title: String, subtitle: String, subtitleColor: Color, desc: String) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = Color.White,
        shadowElevation = 2.dp,
        modifier = Modifier.fillMaxWidth().clickable { /* TODO */ }
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier.size(44.dp).background(iconBgColor, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, contentDescription = null, tint = iconColor, modifier = Modifier.size(20.dp))
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(title, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Color.Black)
                Text(subtitle, color = subtitleColor, fontSize = 12.sp)
                Text(desc, color = Color.Gray, fontSize = 11.sp)
            }
        }
    }
}
