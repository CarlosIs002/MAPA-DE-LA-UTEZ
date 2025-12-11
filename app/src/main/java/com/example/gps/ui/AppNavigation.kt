package com.example.gps.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Article
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Map
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.gps.ui.camera.CameraScreen
import com.example.gps.ui.detail.TripDetailScreen
import com.example.gps.ui.edit.EditPlaceScreen
import com.example.gps.ui.edit.EditTripScreen
import com.example.gps.ui.gallery.GalleryScreen
import com.example.gps.ui.map.MapScreen
import com.example.gps.ui.tracking.TrackingScreen

sealed class Screen(val route: String, val label: String, val icon: ImageVector) {
    object Inicio : Screen("inicio", "Inicio", Icons.Default.Home)
    object Mapa : Screen("mapa", "Mapa", Icons.Default.Map)
    object Reportes : Screen("reportes", "Reportes", Icons.AutoMirrored.Filled.Article)
}

// --- RUTAS CON ARGUMENTOS ---
const val CAMERA_ROUTE_TEMPLATE = "camera"
const val EDIT_TRIP_ROUTE_TEMPLATE = "edit_trip?photoUri={photoUri}"
const val LUGAR_DETAIL_ROUTE_TEMPLATE = "lugar_detail/{lugarId}"
const val EDIT_PLACE_ROUTE_TEMPLATE = "edit_place/{lugarId}"

val bottomNavItems = listOf(
    Screen.Inicio,
    Screen.Mapa,
    Screen.Reportes
)

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    Scaffold(
        bottomBar = {
            NavigationBar {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination
                bottomNavItems.forEach { screen ->
                    NavigationBarItem(
                        icon = { Icon(screen.icon, contentDescription = screen.label) },
                        label = { Text(screen.label) },
                        selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                        onClick = {
                            navController.navigate(screen.route) {
                                popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Inicio.route, // <-- RUTA INICIAL CAMBIADA
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Inicio.route) { TrackingScreen(navController = navController) }
            composable(Screen.Mapa.route) { MapScreen(navController = navController) }
            composable(Screen.Reportes.route) { GalleryScreen(navController = navController) }

            composable(route = CAMERA_ROUTE_TEMPLATE) {
                CameraScreen(navController)
            }

            composable(
                route = EDIT_TRIP_ROUTE_TEMPLATE,
                arguments = listOf(
                    navArgument("photoUri") { type = NavType.StringType; nullable = true }
                )
            ) { backStackEntry ->
                val photoUri = backStackEntry.arguments?.getString("photoUri")
                EditTripScreen(navController, photoUri)
            }

            composable(
                route = LUGAR_DETAIL_ROUTE_TEMPLATE,
                arguments = listOf(navArgument("lugarId") { type = NavType.IntType })
            ) { backStackEntry ->
                val lugarId = backStackEntry.arguments?.getInt("lugarId") ?: -1
                TripDetailScreen(navController, lugarId)
            }

            composable(
                route = EDIT_PLACE_ROUTE_TEMPLATE,
                arguments = listOf(navArgument("lugarId") { type = NavType.IntType })
            ) { backStackEntry ->
                val lugarId = backStackEntry.arguments?.getInt("lugarId") ?: -1
                if (lugarId != -1) {
                    EditPlaceScreen(navController, lugarId)
                }
            }
        }
    }
}