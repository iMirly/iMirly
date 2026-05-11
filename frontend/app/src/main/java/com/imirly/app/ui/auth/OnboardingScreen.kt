package com.imirly.app.ui.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
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
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.weight(1f)
        ) { page ->
            OnboardingPageContent(page)
        }

        // Dots
        Row(
            Modifier
                .height(50.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
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

        Button(
            onClick = {
                if (pagerState.currentPage < 2) {
                    scope.launch { pagerState.animateScrollToPage(pagerState.currentPage + 1) }
                } else {
                    navController.navigate("register")
                }
            },
            modifier = Modifier
                .fillMaxWidth(0.85f)
                .height(55.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color.White),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text(
                if (pagerState.currentPage < 2) "Siguiente >" else "Comenzar",
                color = iMirlyPurple,
                fontWeight = FontWeight.Bold
            )
        }

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

        Text(
            text = "iMirly v1.0.0",
            color = Color.White.copy(alpha = 0.5f),
            fontSize = 10.sp,
            modifier = Modifier.padding(top = 10.dp)
        )
    }
}

@Composable
fun OnboardingPageContent(page: Int) {
    val title = when (page) {
        0 -> "Encuentra profesionales\ncerca de ti"
        1 -> "Conecta fácilmente"
        else -> "Reserva con confianza"
    }

    val imageRes = when (page) {
        0 -> R.drawable.onboarding_1_1
        1 -> R.drawable.onboarding_2_1
        else -> R.drawable.onboarding_3_1
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "iMirly",
            color = Color.White,
            fontSize = 32.sp,
            fontWeight = FontWeight.ExtraBold,
            modifier = Modifier.padding(bottom = 32.dp)
        )


        // Box exterior con borde
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 26.dp)
                .aspectRatio(1f)
                .clip(RoundedCornerShape(52.dp))
                .background(Color.White.copy(alpha = 0.2f)),  // ← recuadro morado de vuelta
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = imageRes),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
                    .clip(RoundedCornerShape(44.dp)),
                contentScale = ContentScale.Crop
            )
        }

        Spacer(modifier = Modifier.height(40.dp))

        Text(
            text = title,
            color = Color.White,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
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

@Preview(showSystemUi = true)
@Composable
fun ObBoardingPreview(){
    val navController = rememberNavController()
    OnboardingScreen(navController = navController)
}