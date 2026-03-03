package com.imirly.app.ui.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.imirly.app.R
import kotlinx.coroutines.launch

@Composable
fun OnboardingScreen(navController: NavController) {
    val pagerState = rememberPagerState(pageCount = { 3 })
    val scope = rememberCoroutineScope()
    val iMirlyPurple = Color(0xFF6C5CE7)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(iMirlyPurple)
            .padding(vertical = 40.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Carrusel de contenido
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.weight(1f)
        ) { page ->
            OnboardingPageContent(page)
        }

        // 1. Indicador de puntos (Dots) - Igual que en tu Figma
        Row(
            Modifier.height(50.dp).fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            repeat(3) { iteration ->
                val color = if (pagerState.currentPage == iteration) Color.White else Color.White.copy(alpha = 0.5f)
                Box(
                    modifier = Modifier
                        .padding(4.dp)
                        .clip(CircleShape)
                        .background(color)
                        .size(10.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // 2. Botón Principal (Siguiente o Comenzar)
        Button(
            onClick = {
                if (pagerState.currentPage < 2) {
                    scope.launch { pagerState.animateScrollToPage(pagerState.currentPage + 1) }
                } else {
                    navController.navigate("login")
                }
            },
            modifier = Modifier.fillMaxWidth(0.85f).height(55.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color.White),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text(
                if (pagerState.currentPage < 2) "Siguiente >" else "Comenzar",
                color = iMirlyPurple,
                fontWeight = FontWeight.Bold
            )
        }

        // 3. Botón Secundario (Omitir o Ya tengo cuenta)
        TextButton(
            onClick = { navController.navigate("login") },
            modifier = Modifier.padding(top = 10.dp)
        ) {
            Text(
                text = if (pagerState.currentPage < 2) "Omitir" else "Ya tengo cuenta",
                color = Color.White,
                fontWeight = FontWeight.Medium
            )
        }

        Text(text = "iMirly v1.0.0", color = Color.White.copy(alpha = 0.5f), fontSize = 10.sp, modifier = Modifier.padding(top = 10.dp))
    }
}

@Composable
fun OnboardingPageContent(page: Int) {
    // 1. Identificar qué imagen toca según la página
    val imageRes = when (page) {
        0 -> R.drawable.onboarding_1
        1 -> R.drawable.onboarding_2
        else -> R.drawable.onboarding_3
    }

    val title = when (page) {
        0 -> "Encuentra profesionales cerca de ti"
        1 -> "Conecta fácilmente"
        else -> "Reserva con confianza"
    }

    val icon = when (page) {
        0 -> Icons.Filled.Search
        1 -> Icons.Filled.Security
        else -> Icons.Filled.Star
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Contenedor de la imagen
        Box(contentAlignment = Alignment.BottomEnd) {
            Surface(
                modifier = Modifier.size(300.dp), // Ajustado según Figma
                shape = RoundedCornerShape(32.dp),
                color = Color.White.copy(alpha = 0.2f)
            ) {
                // AQUÍ SE CARGA LA IMAGEN REAL
                Image(
                    painter = painterResource(id = imageRes),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop // Para que rellene el cuadro
                )
            }

            // Icono flotante característico del diseño
            Surface(
                modifier = Modifier.size(56.dp).offset(x = (10).dp, y = (10).dp),
                shape = RoundedCornerShape(16.dp),
                color = when(page) {
                    0 -> Color(0xFFE0E0FF)
                    1 -> Color(0xFFE0FFE0)
                    else -> Color(0xFFFFF4CC)
                },
                shadowElevation = 6.dp
            ) {
                Icon(
                    icon,
                    contentDescription = null,
                    tint = when(page) {
                        0 -> Color(0xFF6C5CE7)
                        1 -> Color(0xFF2ECC71)
                        else -> Color(0xFFF1C40F)
                    },
                    modifier = Modifier.padding(14.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(50.dp))

        Text(
            text = title,
            color = Color.White,
            fontSize = 26.sp,
            fontWeight = FontWeight.ExtraBold,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = when (page) {
                0 -> "Conecta con expertos en múltiples categorías: hogar, deportes, belleza y más."
                1 -> "Habla directamente con quien ofrece el servicio y acuerda los detalles sin intermediarios."
                else -> "Sistema de valoraciones, pagos seguros y chat directo con profesionales. Todo en una sola app."
            },
            color = Color.White.copy(alpha = 0.8f),
            fontSize = 15.sp,
            textAlign = TextAlign.Center,
            lineHeight = 22.sp
        )
    }
}