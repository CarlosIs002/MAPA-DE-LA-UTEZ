package com.example.gps.ui.map

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.gps.viewmodel.MapViewModel
import com.utsman.osmandcompose.Marker
import com.utsman.osmandcompose.OpenStreetMap
import com.utsman.osmandcompose.rememberCameraState
import com.utsman.osmandcompose.rememberMarkerState
import kotlinx.coroutines.launch
import org.osmdroid.util.GeoPoint

// 1. Estructura de datos unificada para el panel lateral
data class DrawerInfo(val title: String, val details: List<String>)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapScreen(
    viewModel: MapViewModel = viewModel()
) {
    val lugares by viewModel.lugares.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    // 2. El estado ahora almacena la información unificada del panel
    var selectedDrawerInfo by remember { mutableStateOf<DrawerInfo?>(null) }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                // 3. El contenido del panel lee de la nueva estructura
                selectedDrawerInfo?.let { info ->
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(info.title, fontWeight = FontWeight.Bold, fontSize = 20.sp)
                        Divider(modifier = Modifier.padding(vertical = 8.dp))
                        info.details.forEach { detail ->
                            Text(text = detail, modifier = Modifier.padding(bottom = 4.dp))
                        }
                    }
                }
            }
        }
    ) {
        val cameraState = rememberCameraState {
            geoPoint = GeoPoint(18.851252, -99.201060) // UTEZ
            zoom = 18.0
        }

        Box(modifier = Modifier.fillMaxSize()) {
            OpenStreetMap(
                modifier = Modifier.fillMaxSize(),
                cameraState = cameraState
            ) {
                // --- Marcadores de la Base de Datos ---
                lugares.forEach { lugar ->
                    val geoPoint = GeoPoint(lugar.latitud, lugar.longitud)
                    Marker(
                        state = rememberMarkerState(geoPoint = geoPoint),
                        title = lugar.title,
                        onClick = {
                            // 4a. OnClick para marcadores de la BD: convierte Lugar a DrawerInfo
                            selectedDrawerInfo = DrawerInfo(
                                title = lugar.title,
                                details = listOfNotNull(lugar.salon, lugar.descripcion).filter { it.isNotBlank() }
                            )
                            scope.launch { drawerState.open() }
                            true
                        }
                    )
                }

                // --- Marcadores Fijos (Hardcoded) ---
                Marker(
                    state = rememberMarkerState(geoPoint = GeoPoint(18.85045408195868, -99.20137222324546)),
                    onClick = {
                        selectedDrawerInfo = DrawerInfo(
                            title = "-------Rectoria-------",
                            details = listOf("Control Escolar", "Finanzas", "Dirección Académica")
                        )
                        scope.launch { drawerState.open() }
                        true
                    }
                ) { Text(text = "Rectoria", color = Color.Black, fontSize = 12.sp, fontWeight = FontWeight.Bold) }

                Marker(
                    state = rememberMarkerState(geoPoint = GeoPoint(18.85139623679503, -99.20087307973316)),
                    onClick = {
                        selectedDrawerInfo = DrawerInfo(
                            title = "-------Docencia 1-------",
                            details = listOf("Salón 101", "Salón 102", "Salón 103", "Auditorio")
                        )
                        scope.launch { drawerState.open() }
                        true
                    }
                ) { Text(text = "Docencia 1", color = Color.Black, fontSize = 12.sp, fontWeight = FontWeight.Bold) }

                Marker(
                    state = rememberMarkerState(geoPoint = GeoPoint(18.852418469088246, -99.20015652703296)),
                    onClick = {
                        selectedDrawerInfo = DrawerInfo(
                            title = "-------Docencia 4-------",
                            details = listOf("Control Escolar", "Finanzas", "Dirección Académica")
                        )
                        scope.launch { drawerState.open() }
                        true
                    }
                ) { Text(text = "Docencia 4", color = Color.Black, fontSize = 12.sp, fontWeight = FontWeight.Bold) }

                Marker(
                    state = rememberMarkerState(geoPoint = GeoPoint(18.852115607339066, -99.20013329906479)),
                    onClick = {
                        selectedDrawerInfo = DrawerInfo(
                            title = "-------Docencia 3-------",
                            details = listOf("Control Escolar", "Finanzas", "Dirección Académica")
                        )
                        scope.launch { drawerState.open() }
                        true
                    }
                ) { Text(text = "Docencia 3", color = Color.Black, fontSize = 12.sp, fontWeight = FontWeight.Bold) }

                Marker(
                    state = rememberMarkerState(geoPoint = GeoPoint(18.852144916564487, -99.2009953147721)),
                    onClick = {
                        selectedDrawerInfo = DrawerInfo(
                            title = "-------Docencia 5-------",
                            details = listOf("Control Escolar", "Finanzas", "Dirección Académica")
                        )
                        scope.launch { drawerState.open() }
                        true
                    }
                ) { Text(text = "Docencia 5", color = Color.Black, fontSize = 12.sp, fontWeight = FontWeight.Bold) }

                Marker(
                    state = rememberMarkerState(geoPoint = GeoPoint(18.851806567244157, -99.20094534452299)),
                    onClick = {
                        selectedDrawerInfo = DrawerInfo(
                            title = "-------Docencia 2-------",
                            details = listOf("Laboratorio de Redes", "Laboratorio de Software", "Salón 201")
                        )
                        scope.launch { drawerState.open() }
                        true
                    }
                ) { Text(text = "Docencia 2", color = Color.Black, fontSize = 12.sp, fontWeight = FontWeight.Bold) }

                Marker(
                    state = rememberMarkerState(geoPoint = GeoPoint(18.852467317706274, -99.20072948358094)),
                    onClick = {
                        selectedDrawerInfo = DrawerInfo(
                            title = "-------Cedim-------",
                            details = listOf("Control Escolar", "Finanzas", "Dirección Académica")
                        )
                        scope.launch { drawerState.open() }
                        true
                    }
                ) { Text(text = "Cedim", color = Color.Black, fontSize = 12.sp, fontWeight = FontWeight.Bold) }

                Marker(
                    state = rememberMarkerState(geoPoint = GeoPoint(18.851041435400205, -99.20049590361567)),
                    onClick = {
                        selectedDrawerInfo = DrawerInfo(
                            title = "-------Taller pesado 1-------",
                            details = listOf("Control Escolar", "Finanzas", "Dirección Académica")
                        )
                        scope.launch { drawerState.open() }
                        true
                    }
                ) { Text(text = "Taller Pesado 1", color = Color.Black, fontSize = 12.sp, fontWeight = FontWeight.Bold) }

                Marker(
                    state = rememberMarkerState(geoPoint = GeoPoint(18.850149381396278, -99.2000629599035)),
                    onClick = {
                        selectedDrawerInfo = DrawerInfo(
                            title = "-------Taller Pesado 2-------",
                            details = listOf("Control Escolar", "Finanzas", "Dirección Académica")
                        )
                        scope.launch { drawerState.open() }
                        true
                    }
                ) { Text(text = "Taller Pesado 2", color = Color.Black, fontSize = 12.sp, fontWeight = FontWeight.Bold) }

                Marker(
                    state = rememberMarkerState(geoPoint = GeoPoint(18.85059540899096, -99.20067127322059)),
                    onClick = {
                        selectedDrawerInfo = DrawerInfo(
                            title = "-------Biblioteca-------",
                            details = listOf("Control Escolar", "Finanzas", "Dirección Académica")
                        )
                        scope.launch { drawerState.open() }
                        true
                    }
                ) { Text(text = "Biblioteca", color = Color.Black, fontSize = 12.sp, fontWeight = FontWeight.Bold) }

                Marker(
                    state = rememberMarkerState(geoPoint = GeoPoint(18.849903028437538, -99.20127684638763)),
                    onClick = {
                        selectedDrawerInfo = DrawerInfo(
                            title = "-------Ceviset-------",
                            details = listOf("Control Escolar", "Finanzas", "Dirección Académica")
                        )
                        scope.launch { drawerState.open() }
                        true
                    }
                ) { Text(text = "Ceviset", color = Color.Black, fontSize = 12.sp, fontWeight = FontWeight.Bold) }

                Marker(
                    state = rememberMarkerState(geoPoint = GeoPoint(18.849472477045325, -99.20131930266133)),
                    onClick = {
                        selectedDrawerInfo = DrawerInfo(
                            title = "-------Cecadec-------",
                            details = listOf("Control Escolar", "Finanzas", "Dirección Académica")
                        )
                        scope.launch { drawerState.open() }
                        true
                    }
                ) { Text(text = "Cecadec", color = Color.Black, fontSize = 12.sp, fontWeight = FontWeight.Bold) }
            }

            when {
                isLoading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
                error != null -> {
                    Text(
                        text = error!!,
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(16.dp),
                        textAlign = TextAlign.Center,
                        color = Color.Red
                    )
                }
            }
        }
    }
}
