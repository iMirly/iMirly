package com.imirly.app.ui.main

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.imirly.app.network.CategoriaResponse

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController, viewModel: HomeViewModel = viewModel()) {
    val iMirlyPurple = Color(0xFF6C5CE7)
    val lightBackground = Color(0xFFFAFAFA)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(lightBackground)
            .verticalScroll(rememberScrollState())
            .padding(24.dp)
    ) {
        // 1. Cabecera
        Text("iMirly", fontSize = 28.sp, fontWeight = FontWeight.ExtraBold, color = iMirlyPurple)
        Text("Encuentra tu profesional ideal", fontSize = 14.sp, color = Color.Gray)

        Spacer(modifier = Modifier.height(20.dp))

        // 2. Buscador (Ahora es clickable)
        Box(modifier = Modifier.fillMaxWidth().clickable { navController.navigate("search") }) {
            OutlinedTextField(
                value = "",
                onValueChange = {},
                placeholder = { Text("Busca servicios...", color = Color.Gray) },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = Color.Gray) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    disabledContainerColor = Color.White,
                    disabledBorderColor = Color(0xFFEEEEEE),
                    disabledTextColor = Color.Gray,
                    disabledPlaceholderColor = Color.Gray,
                    disabledLeadingIconColor = Color.Gray
                ),
                enabled = false // Desactivado para que no abra el teclado aquí, sino en la siguiente pantalla
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // 3. Banner Promocional
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            color = iMirlyPurple
        ) {
            Column(modifier = Modifier.padding(24.dp)) {
                Surface(
                    color = Color.White.copy(alpha = 0.2f),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("✨ NUEVO", color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp))
                }
                Spacer(modifier = Modifier.height(12.dp))
                Text("Servicios locales,\nsoluciones reales.\niMirly", color = Color.White, fontSize = 22.sp, fontWeight = FontWeight.Bold, lineHeight = 28.sp)
                Spacer(modifier = Modifier.height(8.dp))
                Text("Miles de profesionales listos para ayudarte", color = Color.White.copy(alpha = 0.8f), fontSize = 12.sp)
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = { /* TODO */ },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Explorar servicios", color = iMirlyPurple, fontWeight = FontWeight.Bold)
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // 4. Estadísticas
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            StatItem("2.5K+", "Profesionales")
            StatItem("4.8★", "Valoración")
            StatItem("15K+", "Servicios")
        }

        Spacer(modifier = Modifier.height(32.dp))

        // 5. Categorías
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            Text("Categorías populares", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            Text("Ver todas", fontSize = 12.sp, color = iMirlyPurple, fontWeight = FontWeight.SemiBold)
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (viewModel.isLoading.value) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally), color = iMirlyPurple)
        } else {
            // Cuadrícula de 2 columnas casera para evitar conflictos de scroll
            val categorias = viewModel.categorias.value
            for (i in categorias.indices step 2) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    // Primera tarjeta de la fila
                    CategoryCard(
                        categoria = categorias[i],
                        modifier = Modifier
                            .weight(1f)
                            .clickable {
                                // Navega pasando el ID y el Nombre
                                navController.navigate("subcategories/${categorias[i].id}/${categorias[i].nombre}")
                            }
                    )

                    if (i + 1 < categorias.size) {
                        // Segunda tarjeta de la fila
                        CategoryCard(
                            categoria = categorias[i + 1],
                            modifier = Modifier
                                .weight(1f)
                                .clickable {
                                    navController.navigate("subcategories/${categorias[i+1].id}/${categorias[i+1].nombre}")
                                }
                        )
                    } else {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Composable
fun StatItem(value: String, label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(value, fontSize = 20.sp, fontWeight = FontWeight.ExtraBold)
        Text(label, fontSize = 12.sp, color = Color.Gray)
    }
}

@SuppressLint("DiscouragedApi")
@Composable
fun CategoryCard(categoria: CategoriaResponse, modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier.height(180.dp),
        shape = RoundedCornerShape(20.dp),
        color = Color.White,
        shadowElevation = 2.dp
    ) {
        Column(
            modifier = Modifier.padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            val context = androidx.compose.ui.platform.LocalContext.current

            // Intenta primero con el icono exacto que viene del backend
            // Ej: "deporte_yoga", "deporte", "belleza_facial", etc.
            val imageResId = remember(categoria.icono) {
                val resId = context.resources.getIdentifier(
                    categoria.icono,  // Debe ser exactamente "deporte_yoga" sin extensión
                    "drawable",
                    context.packageName
                )
                // Si no encuentra el icono específico, busca la categoría padre
                // Ej: "deporte_yoga" -> intenta "deporte"
                if (resId == 0) {
                    val parentIcono = categoria.icono.substringBefore("_")
                    val parentResId = context.resources.getIdentifier(
                        parentIcono,
                        "drawable",
                        context.packageName
                    )
                    if (parentResId != 0) parentResId
                    else com.imirly.app.R.drawable.ic_launcher_background
                } else {
                    resId
                }
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)),
                contentAlignment = Alignment.Center
            ) {
                androidx.compose.foundation.Image(
                    painter = androidx.compose.ui.res.painterResource(id = imageResId),
                    contentDescription = categoria.nombre,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(12.dp),
                    contentScale = androidx.compose.ui.layout.ContentScale.Crop
                )
            }

            Spacer(modifier = Modifier.height(8.dp))
            Text(
                categoria.nombre,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF6C5CE7)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                "${categoria.numAnuncios ?: 0} profesionales",
                fontSize = 10.sp,
                color = Color.Gray
            )
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}