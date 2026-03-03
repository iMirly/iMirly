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
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.People
import androidx.compose.material.icons.outlined.Security
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AboutMirlyScreen(navController: NavController) {
    val iMirlyPurple = Color(0xFF6C5CE7)
    val lightBackground = Color(0xFFFAFAFA)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Sobre Mirly", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.SemiBold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) { // TODO: Logic
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = iMirlyPurple
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(lightBackground)
                .verticalScroll(rememberScrollState())
        ) {
            // Header Section with Purple Background
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp)
                    .background(iMirlyPurple)
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = Color.White,
                            modifier = Modifier.size(36.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text("i", color = iMirlyPurple, fontWeight = FontWeight.Bold, fontSize = 24.sp)
                            }
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Mirly", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 36.sp)
                    }
                    Spacer(modifier = Modifier.height(24.dp))
                    Text("Versión 1.0.0", color = Color.White.copy(alpha = 0.8f), fontSize = 12.sp)
                }
            }

            // Foreground Content Card Overlapping the Header
            Box(modifier = Modifier.padding(horizontal = 16.dp)) {
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = Color.White,
                    shadowElevation = 2.dp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .offset(y = (-30).dp)
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .background(iMirlyPurple.copy(alpha = 0.1f), RoundedCornerShape(8.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Outlined.FavoriteBorder, contentDescription = null, tint = iMirlyPurple, modifier = Modifier.size(20.dp))
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        Text("Nuestra misión", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = Color.Black)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            "Conectar a personas que necesitan servicios con profesionales cualificados de manera fácil, rápida y segura. Creemos en crear oportunidades y facilitar la vida de nuestra comunidad.",
                            color = Color.DarkGray,
                            fontSize = 14.sp,
                            lineHeight = 20.sp
                        )
                    }
                }
            }

            // Valores Section
            Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = Color.White,
                    shadowElevation = 2.dp,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Text("Nuestros valores", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = Color.Black)
                        Spacer(modifier = Modifier.height(16.dp))

                        // Confianza
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(modifier = Modifier.size(40.dp).background(Color(0xFFFFEBEE), CircleShape), contentAlignment = Alignment.Center) {
                                Icon(Icons.Outlined.FavoriteBorder, contentDescription = null, tint = Color(0xFFE53935), modifier = Modifier.size(20.dp))
                            }
                            Spacer(modifier = Modifier.width(16.dp))
                            Column {
                                Text("Confianza", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                                Text("Verificamos todos los profesionales\npara garantizar un servicio de calidad", color = Color.Gray, fontSize = 12.sp, lineHeight = 16.sp)
                            }
                        }
                        Spacer(modifier = Modifier.height(16.dp))

                        // Comunidad
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(modifier = Modifier.size(40.dp).background(Color(0xFFE3F2FD), CircleShape), contentAlignment = Alignment.Center) {
                                Icon(Icons.Outlined.People, contentDescription = null, tint = Color(0xFF1E88E5), modifier = Modifier.size(20.dp))
                            }
                            Spacer(modifier = Modifier.width(16.dp))
                            Column {
                                Text("Comunidad", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                                Text("Construimos una red de confianza\nentre usuarios y profesionales", color = Color.Gray, fontSize = 12.sp, lineHeight = 16.sp)
                            }
                        }
                        Spacer(modifier = Modifier.height(16.dp))

                        // Seguridad
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(modifier = Modifier.size(40.dp).background(Color(0xFFE8F5E9), CircleShape), contentAlignment = Alignment.Center) {
                                Icon(Icons.Outlined.Security, contentDescription = null, tint = Color(0xFF43A047), modifier = Modifier.size(20.dp))
                            }
                            Spacer(modifier = Modifier.width(16.dp))
                            Column {
                                Text("Seguridad", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                                Text("Protegemos tus datos y transacciones con\nla máxima seguridad", color = Color.Gray, fontSize = 12.sp, lineHeight = 16.sp)
                            }
                        }
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))

            // Stats Section
            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                StatCard(value = "50K+", label = "Usuarios", modifier = Modifier.weight(1f))
                Spacer(modifier = Modifier.width(8.dp))
                StatCard(value = "10K+", label = "Profesionales", modifier = Modifier.weight(1f))
                Spacer(modifier = Modifier.width(8.dp))
                StatCard(value = "100K+", label = "Servicios", modifier = Modifier.weight(1f))
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Links Section
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = Color.White,
                shadowElevation = 2.dp,
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)
            ) {
                Column {
                    AboutLinkItem("Términos y condiciones") { /* TODO */ }
                    Divider(color = Color(0xFFF0F0F0))
                    AboutLinkItem("Política de privacidad") { /* TODO */ }
                    Divider(color = Color(0xFFF0F0F0))
                    AboutLinkItem("Licencias") { /* TODO */ }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                "© 2026 iMirly. Todos los derechos reservados.",
                color = Color.Gray,
                fontSize = 10.sp,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}

@Composable
fun StatCard(value: String, label: String, modifier: Modifier = Modifier) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = Color.White,
        shadowElevation = 2.dp,
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.padding(vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(value, fontWeight = FontWeight.Bold, fontSize = 18.sp, color = Color(0xFF6C5CE7))
            Text(label, color = Color.Gray, fontSize = 11.sp, textAlign = TextAlign.Center)
        }
    }
}

@Composable
fun AboutLinkItem(title: String, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(title, fontSize = 14.sp, color = Color.DarkGray)
        Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = null, tint = Color.LightGray)
    }
}
