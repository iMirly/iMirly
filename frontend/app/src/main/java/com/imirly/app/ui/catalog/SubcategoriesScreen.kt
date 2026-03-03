package com.imirly.app.ui.catalog

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController


// 2. LA PANTALLA: Estructura visual basada en tu Figma
@Composable
fun SubcategoriesScreen(navController: NavController, categoriaId: String, categoriaNombre: String) {
    val viewModel: SubcategoriesViewModel = viewModel()
    val iMirlyPurple = Color(0xFF6C5CE7)

    // Se ejecuta una sola vez al entrar en la pantalla
    LaunchedEffect(categoriaId) {
        viewModel.cargarSubcategorias(categoriaId)
    }

    Column(
        modifier = Modifier.fillMaxSize().background(Color(0xFFFAFAFA))
    ) {
        // 2. CABECERA UNIVERSAL ESTANDARIZADA
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 24.dp, start = 12.dp, end = 24.dp, bottom = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver", tint = Color.Black)
            }
            Spacer(modifier = Modifier.width(4.dp))
            Text(categoriaNombre, fontSize = 22.sp, fontWeight = FontWeight.Bold)
        }

        // 3. Envolvemos el resto del contenido en una columna que SÍ tenga padding a los lados
        Column(modifier = Modifier.padding(horizontal = 24.dp)) {

            // 👇 MOVEMOS EL BUSCADOR AQUÍ DENTRO 👇
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

        // 3. Envolvemos el resto del contenido en una columna que SÍ tenga padding a los lados
        Column(modifier = Modifier.padding(horizontal = 24.dp)) {
            Text(
                text = "¿Qué servicio necesitas para tu ${categoriaNombre.lowercase()}?",
                fontSize = 16.sp,
                color = Color.Gray,
                lineHeight = 22.sp
            )
            Spacer(modifier = Modifier.height(24.dp))

            if (viewModel.isLoading.value) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = iMirlyPurple)
                }
            } else {
                // Cuadrícula de subcategorías
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(viewModel.subcategorias.value) { sub ->
                        SubcategoryCard(subcategoria = sub) {
                            // Al pulsar, vamos a la lista de anuncios de esa subcategoría
                            navController.navigate("anuncios_list/${sub.id}/${sub.nombre}")
                        }
                    }
                }
            }
        }
        }
    }
}

// 3. EL COMPONENTE (Tarjeta): Diseño simplificado para la cuadrícula
@Composable
fun SubcategoryCard(subcategoria: com.imirly.app.network.SubcategoriaResponse, onClick: () -> Unit) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(130.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(20.dp),
        color = Color.White,
        shadowElevation = 2.dp
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            val context = androidx.compose.ui.platform.LocalContext.current

            val imageResId = androidx.compose.runtime.remember(subcategoria.icono) {
                when (subcategoria.icono) {
                    // Belleza
                    "fitness_center"  -> com.imirly.app.R.drawable.deporte
                    "pets"            -> com.imirly.app.R.drawable.mascotas
                    "home"            -> com.imirly.app.R.drawable.hogar
                    "school"          -> com.imirly.app.R.drawable.clases
                    "face"            -> com.imirly.app.R.drawable.belleza
                    "computer"        -> com.imirly.app.R.drawable.otros
                    "camera"          -> com.imirly.app.R.drawable.otros
                    "spa"             -> com.imirly.app.R.drawable.cuidado
                    // Por si el backend ya devuelve nombres correctos (tras el fix)
                    "belleza"                    -> com.imirly.app.R.drawable.belleza
                    "belleza_depilacion"         -> com.imirly.app.R.drawable.belleza_depilacion
                    "belleza_facial"             -> com.imirly.app.R.drawable.belleza_facial
                    "belleza_maquillaje"         -> com.imirly.app.R.drawable.belleza_maquillaje
                    "belleza_masajes"            -> com.imirly.app.R.drawable.belleza_masajes
                    "belleza_peluqueria"         -> com.imirly.app.R.drawable.belleza_peluqueria
                    "belleza_unas"               -> com.imirly.app.R.drawable.belleza_unas
                    "clases"                     -> com.imirly.app.R.drawable.clases
                    "clases_baile"               -> com.imirly.app.R.drawable.clases_baile
                    "clases_colegio"             -> com.imirly.app.R.drawable.clases_colegio
                    "clases_dibujo"              -> com.imirly.app.R.drawable.clases_dibujo
                    "clases_eso"                 -> com.imirly.app.R.drawable.clases_eso
                    "clases_idiomas"             -> com.imirly.app.R.drawable.clases_idiomas
                    "clases_musica"              -> com.imirly.app.R.drawable.clases_musica
                    "cuidado"                    -> com.imirly.app.R.drawable.cuidado
                    "cuidados_ancianos"          -> com.imirly.app.R.drawable.cuidados_ancianos
                    "cuidados_ninos"             -> com.imirly.app.R.drawable.cuidados_ninos
                    "deporte"                    -> com.imirly.app.R.drawable.deporte
                    "deporte_boxeo"              -> com.imirly.app.R.drawable.deporte_boxeo
                    "deporte_entrenador_personal"-> com.imirly.app.R.drawable.deporte_entrenador_personal
                    "deporte_padel"              -> com.imirly.app.R.drawable.deporte_padel
                    "deporte_pilates"            -> com.imirly.app.R.drawable.deporte_pilates
                    "deporte_tenis"              -> com.imirly.app.R.drawable.deporte_tenis
                    "deporte_yoga"               -> com.imirly.app.R.drawable.deporte_yoga
                    "hogar"                      -> com.imirly.app.R.drawable.hogar
                    "hogar_electricista"         -> com.imirly.app.R.drawable.hogar_electricista
                    "hogar_electrodomesticos"    -> com.imirly.app.R.drawable.hogar_electrodomesticos
                    "hogar_fontaneria"           -> com.imirly.app.R.drawable.hogar_fontaneria
                    "hogar_jardineria"           -> com.imirly.app.R.drawable.hogar_jardineria
                    "hogar_limpieza"             -> com.imirly.app.R.drawable.hogar_limpieza
                    "hogar_mudanzas"             -> com.imirly.app.R.drawable.hogar_mudanzas
                    "hogar_pintura"              -> com.imirly.app.R.drawable.hogar_pintura
                    "hogar_plancha"              -> com.imirly.app.R.drawable.hogar_plancha
                    "hogar_reformas"             -> com.imirly.app.R.drawable.hogar_reformas
                    "mascotas"                   -> com.imirly.app.R.drawable.mascotas
                    "mascotas_adiestrador"       -> com.imirly.app.R.drawable.mascotas_adiestrador
                    "mascotas_conducta"          -> com.imirly.app.R.drawable.mascotas_conducta
                    "mascotas_cuidador"          -> com.imirly.app.R.drawable.mascotas_cuidador
                    "mascotas_paseador"          -> com.imirly.app.R.drawable.mascotas_paseador
                    "mascotas_peluqueria"        -> com.imirly.app.R.drawable.mascotas_peluqueria
                    else -> com.imirly.app.R.drawable.otros
                }
            }

            Box(
                modifier = Modifier
                    .size(54.dp)
                    .clip(RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) {
                androidx.compose.foundation.Image(
                    painter = androidx.compose.ui.res.painterResource(id = imageResId),
                    contentDescription = subcategoria.nombre,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = androidx.compose.ui.layout.ContentScale.Crop
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = subcategoria.nombre,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                color = Color.Black
            )
        }
    }
}