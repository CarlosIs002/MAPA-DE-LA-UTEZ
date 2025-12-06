package com.example.gps.ui.map

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.gps.viewmodel.MapViewModel
import com.utsman.osmandcompose.Marker
import com.utsman.osmandcompose.OpenStreetMap
import com.utsman.osmandcompose.Polyline
import com.utsman.osmandcompose.rememberCameraState
import com.utsman.osmandcompose.rememberMarkerState
import kotlinx.coroutines.launch
import org.osmdroid.util.GeoPoint

@Composable
fun MapScreen(
    viewModel: MapViewModel = viewModel()
) {
    val allPoints by viewModel.allPoints.collectAsState(initial = emptyList())
    val pointsByTrip = allPoints.groupBy { it.tripId }

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    // 1. ESTADO PARA LA LISTA Y TÍTULO DEL PANEL
    var currentDrawerTitle by remember { mutableStateOf("") }
    var currentDrawerItems by remember { mutableStateOf(emptyList<String>()) }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            // 2. EL CONTENIDO DEL PANEL AHORA USA EL ESTADO
            ModalDrawerSheet {
                Text(currentDrawerTitle, modifier = Modifier.padding(16.dp))
                LazyColumn {
                    items(currentDrawerItems) { itemName ->
                        Text(
                            text = itemName,
                            modifier = Modifier
                                .fillParentMaxWidth()
                                .padding(vertical = 12.dp, horizontal = 16.dp)
                        )
                    }
                }
            }
        }
    ) {
        val cameraState = rememberCameraState {
            geoPoint = GeoPoint(18.85125231651032, -99.2010604623713) // UTEZ
            zoom = 18.8 // Zoom para ver la universidad
        }

        LaunchedEffect(allPoints) {
            allPoints.firstOrNull()?.let { firstPoint ->
                val firstGeoPoint = GeoPoint(firstPoint.latitude, firstPoint.longitude)
                cameraState.geoPoint = firstGeoPoint
                cameraState.zoom = 15.0
            }
        }

        OpenStreetMap(
            modifier = Modifier.fillMaxSize(),
            cameraState = cameraState
        ) {
            // --- RECTORIA MARKER ---
            Marker(
                state = rememberMarkerState(
                    geoPoint = GeoPoint(18.85045408195868, -99.20137222324546)
                ),
                title = "Rectoria",
                visible = true,
                onClick = {
                    // 3. ACTUALIZAR ESTADO Y ABRIR PANEL
                    currentDrawerTitle = "-------Rectoria-------"
                    currentDrawerItems = listOf("Control Escolar", "Finanzas", "Dirección Académica")
                    scope.launch { drawerState.open() }
                    true
                }
            ) {
                Text(text = "Rectoria", color = Color.Black, fontSize = 12.sp)
            }

            //Docencia 1
            Marker(
                state = rememberMarkerState(
                    geoPoint = GeoPoint(18.85139623679503, -99.20087307973316)
                ),
                title = "Docencia 1",
                visible = true,
                onClick = {
                    // 3. ACTUALIZAR ESTADO CON DATOS DIFERENTES
                    currentDrawerTitle = "-------Docencia 1-------"
                    currentDrawerItems = listOf("Salón 101", "Salón 102", "Salón 103", "Auditorio")
                    scope.launch { drawerState.open() }
                    true
                }
            ) {
                Text(text = "Docencia 1", color = Color.Black, fontSize = 12.sp)
            }

            //Docencia 4
            Marker(
                state = rememberMarkerState(
                    geoPoint = GeoPoint(18.852418469088246, -99.20015652703296)
                ),
                title = "Docencia 4",
                visible = true,
                onClick = {
                    // 3. ACTUALIZAR ESTADO Y ABRIR PANEL
                    currentDrawerTitle = "-------Docencia 4-------"
                    currentDrawerItems = listOf("Control Escolar", "Finanzas", "Dirección Académica")
                    scope.launch { drawerState.open() }
                    true
                }
            ) {
                Text(text = "Docencia 4", color = Color.Black, fontSize = 12.sp)
            }

            //Docencia 3
            Marker(
                state = rememberMarkerState(
                    geoPoint = GeoPoint(18.852115607339066, -99.20013329906479)
                ),
                title = "Docencia 3",
                visible = true,
                onClick = {
                    // 3. ACTUALIZAR ESTADO Y ABRIR PANEL
                    currentDrawerTitle = "-------Docencia 3-------"
                    currentDrawerItems = listOf("Control Escolar", "Finanzas", "Dirección Académica")
                    scope.launch { drawerState.open() }
                    true
                }
            ) {
                Text(text = "Docencia 3", color = Color.Black, fontSize = 12.sp)
            }

            //Docencia 5
            Marker(
                state = rememberMarkerState(
                    geoPoint = GeoPoint(18.852144916564487, -99.2009953147721)
                ),
                title = "Docencia 5",
                visible = true,
                onClick = {
                    // 3. ACTUALIZAR ESTADO Y ABRIR PANEL
                    currentDrawerTitle = "-------Docencia 5-------"
                    currentDrawerItems = listOf("Control Escolar", "Finanzas", "Dirección Académica")
                    scope.launch { drawerState.open() }
                    true
                }
            ) {
                Text(text = "Docencia 5", color = Color.Black, fontSize = 12.sp)
            }

            //Docencia 2
            Marker(
                state = rememberMarkerState(
                    geoPoint = GeoPoint(18.851806567244157, -99.20094534452299)
                ),
                title = "Docencia 2",
                visible = true,
                onClick = {
                    currentDrawerTitle = "-------Docencia 2-------"
                    currentDrawerItems = listOf("Laboratorio de Redes", "Laboratorio de Software", "Salón 201")
                    scope.launch { drawerState.open() }
                    true
                }
            ) {
                Text(text = "Docencia 2", color = Color.Black, fontSize = 12.sp)
            }

            //Cedim
            Marker(
                state = rememberMarkerState(
                    geoPoint = GeoPoint(18.852467317706274, -99.20072948358094)
                ),
                title = "Cedim",
                visible = true,
                onClick = {
                    // 3. ACTUALIZAR ESTADO Y ABRIR PANEL
                    currentDrawerTitle = "-------Cedim-------"
                    currentDrawerItems = listOf("Control Escolar", "Finanzas", "Dirección Académica")
                    scope.launch { drawerState.open() }
                    true
                }
            ) {
                Text(text = "Cedim", color = Color.Black, fontSize = 12.sp)
            }

            //Taller pesado 1
            Marker(
                state = rememberMarkerState(
                    geoPoint = GeoPoint(18.851041435400205, -99.20049590361567)
                ),
                title = "Taller Pesado 1",
                visible = true,
                onClick = {
                    // 3. ACTUALIZAR ESTADO Y ABRIR PANEL
                    currentDrawerTitle = "-------Taller pesado 1-------"
                    currentDrawerItems = listOf("Control Escolar", "Finanzas", "Dirección Académica")
                    scope.launch { drawerState.open() }
                    true
                }
            ) {
                Text(text = "Taller Pesado 1", color = Color.Black, fontSize = 12.sp)
            }

            //Taller pesado 2
            Marker(
                state = rememberMarkerState(
                    geoPoint = GeoPoint(18.850149381396278, -99.2000629599035)
                ),
                title = "Taller Pesado 2",
                visible = true,
                onClick = {
                    // 3. ACTUALIZAR ESTADO Y ABRIR PANEL
                    currentDrawerTitle = "-------Taller Pesado 2-------"
                    currentDrawerItems = listOf("Control Escolar", "Finanzas", "Dirección Académica")
                    scope.launch { drawerState.open() }
                    true
                }
            ) {
                Text(text = "Taller Pesado 2", color = Color.Black, fontSize = 12.sp)
            }

            //Biblioteca
            Marker(
                state = rememberMarkerState(
                    geoPoint = GeoPoint(18.85059540899096, -99.20067127322059)
                ),
                title = "Biblioteca",
                visible = true,
                onClick = {
                    // 3. ACTUALIZAR ESTADO Y ABRIR PANEL
                    currentDrawerTitle = "-------Biblioteca-------"
                    currentDrawerItems = listOf("Control Escolar", "Finanzas", "Dirección Académica")
                    scope.launch { drawerState.open() }
                    true
                }
            ) {
                Text(text = "Biblioteca", color = Color.Black, fontSize = 12.sp)
            }

            //Ceviset
            Marker(
                state = rememberMarkerState(
                    geoPoint = GeoPoint(18.849903028437538, -99.20127684638763)
                ),
                title = "Ceviset",
                visible = true,
                onClick = {
                    // 3. ACTUALIZAR ESTADO Y ABRIR PANEL
                    currentDrawerTitle = "-------Ceviset-------"
                    currentDrawerItems = listOf("Control Escolar", "Finanzas", "Dirección Académica")
                    scope.launch { drawerState.open() }
                    true
                }
            ) {
                Text(text = "Ceviset", color = Color.Black, fontSize = 12.sp)
            }

            //Cecadec
            Marker(
                state = rememberMarkerState(
                    geoPoint = GeoPoint(18.849472477045325, -99.20131930266133)
                ),
                title = "Cecadec",
                visible = true,
                onClick = {
                    // 3. ACTUALIZAR ESTADO Y ABRIR PANEL
                    currentDrawerTitle = "-------Cecadec-------"
                    currentDrawerItems = listOf("Control Escolar", "Finanzas", "Dirección Académica")
                    scope.launch { drawerState.open() }
                    true
                }
            ) {
                Text(text = "Cecadec", color = Color.Black, fontSize = 12.sp)
            }

            pointsByTrip.forEach { (_, points) ->
                val geoPointList = points.map { point -> GeoPoint(point.latitude, point.longitude) }
                if (geoPointList.size >= 2) {
                    Polyline(geoPoints = geoPointList)
                }
            }
        }
    }
}
