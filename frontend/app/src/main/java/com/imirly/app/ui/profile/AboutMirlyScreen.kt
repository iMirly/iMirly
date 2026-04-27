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
                    IconButton(onClick = { navController.popBackStack() }) {
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
                .padding(innerPadding) // Aplicamos el padding para que no se oculte nada tras la TopAppBar
                .background(lightBackground)
                .verticalScroll(rememberScrollState())
        ) {
            // Sección de cabecera (Logo y Versión)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp) // Altura ajustada ya que no solapa con la barra
                    .background(iMirlyPurple),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    // EL LOGO: Ahora visible
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
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    // LA VERSIÓN: Destacada
                    Surface(
                        color = Color.White.copy(alpha = 0.2f),
                        shape = RoundedCornerShape(20.dp)
                    ) {
                        Text(
                            "Versión 1.0.0", 
                            color = Color.White, 
                            fontSize = 13.sp, 
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
                        )
                    }
                }
            }

            // El resto del contenido se mantiene igual pero con los offsets ajustados
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

            Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = Color.White,
                    shadowElevation = 2.dp,
                    modifier = Modifier.fillMaxWidth().offset(y = (-10).dp)
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Text("Nuestros valores", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = Color.Black)
                        Spacer(modifier = Modifier.height(16.dp))

                        ValueItem(Icons.Outlined.FavoriteBorder, "Confianza", "Verificamos todos los profesionales\npara garantizar un servicio de calidad", Color(0xFFE53935), Color(0xFFFFEBEE))
                        Spacer(modifier = Modifier.height(16.dp))
                        ValueItem(Icons.Outlined.People, "Comunidad", "Construimos una red de confianza\nentre usuarios y profesionales", Color(0xFF1E88E5), Color(0xFFE3F2FD))
                        Spacer(modifier = Modifier.height(16.dp))
                        ValueItem(Icons.Outlined.Security, "Seguridad", "Protegemos tus datos y transacciones con\nla máxima seguridad", Color(0xFF43A047), Color(0xFFE8F5E9))
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(8.dp))

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

            Surface(
                shape = RoundedCornerShape(16.dp),
                color = Color.White,
                shadowElevation = 2.dp,
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)
            ) {
                Column {
                    AboutLinkItem("Términos y condiciones") { }
                    HorizontalDivider(color = Color(0xFFF0F0F0))
                    AboutLinkItem("Política de privacidad") { }
                    HorizontalDivider(color = Color(0xFFF0F0F0))
                    AboutLinkItem("Licencias") { }
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
fun ValueItem(icon: androidx.compose.ui.graphics.vector.ImageVector, title: String, desc: String, tint: Color, bg: Color) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(modifier = Modifier.size(40.dp).background(bg, CircleShape), contentAlignment = Alignment.Center) {
            Icon(icon, contentDescription = null, tint = tint, modifier = Modifier.size(20.dp))
        }
        Spacer(modifier = Modifier.width(16.dp))
        Column {
            Text(title, fontWeight = FontWeight.Bold, fontSize = 14.sp)
            Text(desc, color = Color.Gray, fontSize = 12.sp, lineHeight = 16.sp)
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
