package com.imirly.app.ui.main

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.imirly.app.ui.catalog.AnuncioDetailScreen
import com.imirly.app.ui.catalog.AnunciosListScreen
import com.imirly.app.ui.profile.ProfileScreen
import com.imirly.app.ui.publish.PublishScreen
import com.imirly.app.ui.catalog.SubcategoriesScreen

// Definimos los elementos de la barra inferior
sealed class BottomNavItem(val route: String, val title: String, val icon: ImageVector) {
    object Home : BottomNavItem("home", "Inicio", Icons.Default.Home)
    object Favorites : BottomNavItem("favorites", "Favoritos", Icons.Default.FavoriteBorder)
    object Publish : BottomNavItem("publish", "Publicar", Icons.Default.AddCircleOutline)
    object Messages : BottomNavItem("messages", "Mensajes", Icons.Outlined.Email)
    object Profile : BottomNavItem("profile", "Perfil", Icons.Outlined.Person)
}

@Composable
fun MainScreen(rootNavController: NavHostController) {
    val bottomNavController = rememberNavController()
    val iMirlyPurple = Color(0xFF6C5CE7)

    val items = listOf(
        BottomNavItem.Home,
        BottomNavItem.Favorites,
        BottomNavItem.Publish,
        BottomNavItem.Messages,
        BottomNavItem.Profile
    )

    Scaffold(
        bottomBar = {
            NavigationBar(
                containerColor = Color.White,
                contentColor = Color.Gray
            ) {
                val navBackStackEntry by bottomNavController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route

                items.forEach { item ->
                    NavigationBarItem(
                        icon = { Icon(item.icon, contentDescription = item.title) },
                        label = { Text(item.title, fontSize = 10.sp) },
                        selected = currentRoute == item.route,
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = iMirlyPurple,
                            selectedTextColor = iMirlyPurple,
                            indicatorColor = Color(0xFFE0E0FF), // Fondo sutil al seleccionar
                            unselectedIconColor = Color.Gray,
                            unselectedTextColor = Color.Gray
                        ),
                        onClick = {
                            if (currentRoute != item.route) {
                                bottomNavController.navigate(item.route) {
                                    popUpTo(bottomNavController.graph.startDestinationId) { saveState = true }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        // Aquí cargamos las pantallas según la pestaña seleccionada
        // ... dentro de MainScreen.kt ...
        NavHost(
            navController = bottomNavController,
            startDestination = BottomNavItem.Home.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            // AHORA PASAMOS EL bottomNavController A LA HOME
            composable(BottomNavItem.Home.route) {
                HomeScreen(bottomNavController)
            }

            // NUEVA RUTA: Recibe el ID y el Nombre como parámetros
            composable("subcategories/{id}/{nombre}") { backStackEntry ->
                val id = backStackEntry.arguments?.getString("id") ?: ""
                val nombre = backStackEntry.arguments?.getString("nombre") ?: ""

                SubcategoriesScreen(
                    navController = bottomNavController,
                    categoriaId = id,
                    categoriaNombre = nombre
                )
            }

            // (Opcional) Deja ya lista la ruta para el siguiente paso: la lista de anuncios
            composable("anuncios_list/{id}/{nombre}") { backStackEntry ->
                val id = backStackEntry.arguments?.getString("id") ?: ""
                val nombre = backStackEntry.arguments?.getString("nombre") ?: ""

                AnunciosListScreen(
                    navController = bottomNavController,
                    subcategoriaId = id,
                    subcategoriaNombre = nombre
                )
            }

            composable("anuncio_detail/{id}") { backStackEntry ->
                val id = backStackEntry.arguments?.getString("id") ?: ""
                AnuncioDetailScreen(navController = bottomNavController, anuncioId = id)
            }

            composable(BottomNavItem.Favorites.route) {
                com.imirly.app.ui.catalog.FavoritosScreen(navController = bottomNavController)
            }

            composable(BottomNavItem.Publish.route) {
                PublishScreen(
                    onPublishSuccess = {
                        // 1. Movemos la pestaña inferior al Perfil de forma invisible
                        bottomNavController.navigate(BottomNavItem.Profile.route) {
                            popUpTo(bottomNavController.graph.startDestinationId) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                        // 2. Abrimos la pantalla de Mis Anuncios por encima
                        rootNavController.navigate("mis_anuncios")
                    }
                )
            }

            // 👇 RUTA DEL BUSCADOR CORREGIDA 👇
            composable(
                route = "search?categoriaId={categoriaId}",
                arguments = listOf(navArgument("categoriaId") {
                    type = NavType.StringType // <--- ESTO ES LO QUE FALTABA
                    nullable = true
                    defaultValue = null
                })
            ) { backStackEntry ->
                val categoriaId = backStackEntry.arguments?.getString("categoriaId")
                com.imirly.app.ui.catalog.SearchScreen(
                    navController = bottomNavController,
                    categoriaId = categoriaId
                )
            }

            composable(BottomNavItem.Messages.route) {
                com.imirly.app.ui.messages.MessagesScreen(navController = bottomNavController)            }

            // NUEVA RUTA: Pantalla de Chat (Añadimos soyElProveedor al final)
            composable("chat/{otroUsuarioId}/{nombre}/{solicitudId}/{soyElProveedor}") { backStackEntry ->
                val otroUsuarioId = backStackEntry.arguments?.getString("otroUsuarioId") ?: ""
                val nombre = backStackEntry.arguments?.getString("nombre") ?: "Usuario"
                val solicitudId = backStackEntry.arguments?.getString("solicitudId") ?: ""
                // 👇 Recuperamos si somos el proveedor 👇
                val soyElProveedor = backStackEntry.arguments?.getString("soyElProveedor")?.toBoolean() ?: false

                com.imirly.app.ui.chat.ChatScreen(
                    navController = bottomNavController,
                    otroUsuarioId = otroUsuarioId,
                    nombreContacto = nombre,
                    solicitudId = solicitudId,
                    soyElProveedor = soyElProveedor // <--- SE LO PASAMOS
                )
            }

            composable(BottomNavItem.Profile.route) { ProfileScreen(rootNavController) }
        }
    }
}

// Una pantalla temporal (Placeholder) para que no dé error mientras las construimos
@Composable
fun DummyScreen(texto: String) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(text = texto, fontSize = 20.sp, color = Color.Gray)
    }
}