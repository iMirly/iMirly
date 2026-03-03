package com.imirly.app.ui.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.*
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
fun HelpCenterScreen(navController: NavController) {
    val lightBackground = Color(0xFFFAFAFA)
    val iMirlyPurple = Color(0xFF6C5CE7)
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Centro de ayuda", fontWeight = FontWeight.Bold, fontSize = 18.sp) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
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
            // Search Bar
            TextField(
                value = "",
                onValueChange = {},
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                placeholder = { Text("Buscar artículos de ayuda...", color = Color.Gray, fontSize = 14.sp) },
                leadingIcon = { Icon(Icons.Filled.Search, contentDescription = null, tint = Color.Gray) },
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color(0xFFF0F0F5),
                    unfocusedContainerColor = Color(0xFFF0F0F5),
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                shape = RoundedCornerShape(25.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Categorías Title
            Text("Categorías", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = Color.Black)
            Spacer(modifier = Modifier.height(16.dp))

            // Categorías Grid
            Row(modifier = Modifier.fillMaxWidth()) {
                CategoryCard(
                    icon = Icons.Outlined.Smartphone,
                    iconBgColor = Color(0xFFE8EAF6),
                    iconContentColor = Color(0xFF3F51B5),
                    title = "Primeros pasos",
                    desc = "Cómo empezar a usar\nMirly",
                    count = "12 artículos",
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.width(16.dp))
                CategoryCard(
                    icon = Icons.Outlined.Payment,
                    iconBgColor = Color(0xFFE8F5E9),
                    iconContentColor = Color(0xFF4CAF50),
                    title = "Pagos y saldo",
                    desc = "Gestiona tus\ntransacciones",
                    count = "8 artículos",
                    modifier = Modifier.weight(1f)
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Row(modifier = Modifier.fillMaxWidth()) {
                CategoryCard(
                    icon = Icons.Outlined.Security,
                    iconBgColor = Color(0xFFE3F2FD),
                    iconContentColor = Color(0xFF2196F3),
                    title = "Seguridad",
                    desc = "Mantén tu cuenta\nsegura",
                    count = "10 artículos",
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.width(16.dp))
                CategoryCard(
                    icon = Icons.Outlined.ChatBubbleOutline,
                    iconBgColor = Color(0xFFFFF3E0),
                    iconContentColor = Color(0xFFFF9800),
                    title = "Mensajería",
                    desc = "Cómo chatear con\nprofesionales",
                    count = "6 artículos",
                    modifier = Modifier.weight(1f)
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Row(modifier = Modifier.fillMaxWidth()) {
                CategoryCard(
                    icon = Icons.Outlined.StarBorder,
                    iconBgColor = Color(0xFFFFF8E1),
                    iconContentColor = Color(0xFFFFC107),
                    title = "Valoraciones",
                    desc = "Sistema de reseñas\ny puntuaciones",
                    count = "5 artículos",
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.width(16.dp))
                CategoryCard(
                    icon = Icons.Outlined.Article,
                    iconBgColor = Color(0xFFF3E5F5),
                    iconContentColor = Color(0xFF9C27B0),
                    title = "Publicar servicios",
                    desc = "Guía para profesionales\ny usuarios",
                    count = "15 artículos",
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Preguntas frecuentes Title
            Text("Preguntas frecuentes", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = Color.Black)
            Spacer(modifier = Modifier.height(16.dp))

            Surface(
                shape = RoundedCornerShape(16.dp),
                color = Color.White,
                shadowElevation = 2.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column {
                    FaqItem("¿Cómo puedo contactar con un profesional?", "General")
                    Divider(color = Color(0xFFF0F0F0))
                    FaqItem("¿Es seguro el pago en la plataforma?", "Pagos")
                    Divider(color = Color(0xFFF0F0F0))
                    FaqItem("¿Cómo puedo publicar mi servicio?", "Profesionales")
                    Divider(color = Color(0xFFF0F0F0))
                    FaqItem("¿Puedo cancelar un servicio contratado?", "General")
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Banner Bottom
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = iMirlyPurple,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(Icons.Outlined.ChatBubbleOutline, contentDescription = null, tint = Color.White, modifier = Modifier.size(32.dp))
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("¿No encuentras lo que buscas?", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color.White)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        "Nuestro equipo de soporte está aquí\npara ayudarte",
                        color = Color.White.copy(alpha = 0.8f),
                        fontSize = 12.sp,
                        textAlign = TextAlign.Center,
                        lineHeight = 16.sp
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = { /* TODO */ },
                        colors = ButtonDefaults.buttonColors(containerColor = Color.White, contentColor = iMirlyPurple),
                        shape = RoundedCornerShape(24.dp)
                    ) {
                        Text("Contactar soporte", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    }
                }
            }
            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}

@Composable
fun CategoryCard(icon: ImageVector, iconBgColor: Color, iconContentColor: Color, title: String, desc: String, count: String, modifier: Modifier = Modifier) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = Color.White,
        shadowElevation = 2.dp,
        modifier = modifier.clickable { /* TODO */ }
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .background(iconBgColor, RoundedCornerShape(8.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, contentDescription = null, tint = iconContentColor, modifier = Modifier.size(20.dp))
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text(title, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Color.Black)
            Spacer(modifier = Modifier.height(4.dp))
            Text(desc, color = Color.Gray, fontSize = 10.sp, lineHeight = 14.sp)
            Spacer(modifier = Modifier.height(8.dp))
            Text(count, color = Color.LightGray, fontSize = 10.sp)
        }
    }
}

@Composable
fun FaqItem(question: String, tag: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { /* TODO */ }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(question, fontWeight = FontWeight.SemiBold, fontSize = 13.sp, color = Color.DarkGray)
            Spacer(modifier = Modifier.height(4.dp))
            Surface(
                shape = RoundedCornerShape(4.dp),
                color = Color(0xFFF0F0F5)
            ) {
                Text(tag, fontSize = 10.sp, color = Color(0xFF6C5CE7), modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp))
            }
        }
        Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = null, tint = Color.LightGray)
    }
}
