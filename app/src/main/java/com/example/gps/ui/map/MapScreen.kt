package com.example.gps.ui.map

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.gps.R
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
    val context = LocalContext.current

    val resizedIcon = remember(context) {
        getResizedDrawable(context, R.drawable.locationpoint, 50, 50)
    }

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    // 2. El estado ahora almacena la información unificada del panel
    var selectedDrawerInfo by remember { mutableStateOf<DrawerInfo?>(null) }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                selectedDrawerInfo?.let { info ->
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                brush = Brush.verticalGradient(
                                    colors = listOf(Color(0xFF6200EE), Color(0xFF3700B3))
                                )
                            )
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = info.title.replace("-------", ""),
                            fontWeight = FontWeight.Bold,
                            fontSize = 24.sp,
                            color = Color.White,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Divider(color = Color.White.copy(alpha = 0.5f))
                    }
                    LazyColumn(
                        modifier = Modifier.padding(horizontal = 16.dp)
                    ) {
                        items(info.details) { detail ->
                            Text(
                                text = detail,
                                modifier = Modifier
                                    .padding(vertical = 8.dp)
                                    .fillMaxWidth(),
                                fontSize = 16.sp
                            )
                            Divider()
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
                    val locationPoint = GeoPoint(lugar.latitud, lugar.longitud)
                    Marker(
                        state = rememberMarkerState(geoPoint = locationPoint),
                        title = lugar.title,
                        icon = resizedIcon,
                        onClick = {
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
                    icon = resizedIcon,
                    onClick = {
                        selectedDrawerInfo = DrawerInfo(
                            title = "-------Rectoria-------",
                            details = listOf("Control Escolar", "Finanzas", "Dirección Académica")
                        )
                        scope.launch { drawerState.open() }
                        true
                    }
                ) 

                Marker(
                    state = rememberMarkerState(geoPoint = GeoPoint(18.85139623679503, -99.20087307973316)),
                    icon = resizedIcon,
                    onClick = {
                        selectedDrawerInfo = DrawerInfo(
                            title = "-------Docencia 1-------",
                            details = listOf("Salón 101", "Salón 102", "Salón 103", "Auditorio")
                        )
                        scope.launch { drawerState.open() }
                        true
                    }
                ) 

                Marker(
                    state = rememberMarkerState(geoPoint = GeoPoint(18.852418469088246, -99.20015652703296)),
                    icon = resizedIcon,
                    onClick = {
                        selectedDrawerInfo = DrawerInfo(
                            title = "-------Docencia 4-------",
                            details = listOf("Control Escolar", "Finanzas", "Dirección Académica")
                        )
                        scope.launch { drawerState.open() }
                        true
                    }
                )

                Marker(
                    state = rememberMarkerState(geoPoint = GeoPoint(18.852115607339066, -99.20013329906479)),
                    icon = resizedIcon,
                    onClick = {
                        selectedDrawerInfo = DrawerInfo(
                            title = "-------Docencia 3-------",
                            details = listOf("Control Escolar", "Finanzas", "Dirección Académica")
                        )
                        scope.launch { drawerState.open() }
                        true
                    }
                ) 

                Marker(
                    state = rememberMarkerState(geoPoint = GeoPoint(18.852144916564487, -99.2009953147721)),
                    icon = resizedIcon,
                    onClick = {
                        selectedDrawerInfo = DrawerInfo(
                            title = "-------Docencia 5-------",
                            details = listOf("Control Escolar", "Finanzas", "Dirección Académica")
                        )
                        scope.launch { drawerState.open() }
                        true
                    }
                )

                Marker(
                    state = rememberMarkerState(geoPoint = GeoPoint(18.851806567244157, -99.20094534452299)),
                    icon = resizedIcon,
                    onClick = {
                        selectedDrawerInfo = DrawerInfo(
                            title = "-------Docencia 2-------",
                            details = listOf("Laboratorio de Redes", "Laboratorio de Software", "Salón 201")
                        )
                        scope.launch { drawerState.open() }
                        true
                    }
                ) 

                Marker(
                    state = rememberMarkerState(geoPoint = GeoPoint(18.852467317706274, -99.20072948358094)),
                    icon = resizedIcon,
                    onClick = {
                        selectedDrawerInfo = DrawerInfo(
                            title = "-------Cedim-------",
                            details = listOf("Control Escolar", "Finanzas", "Dirección Académica")
                        )
                        scope.launch { drawerState.open() }
                        true
                    }
                ) 

                Marker(
                    state = rememberMarkerState(geoPoint = GeoPoint(18.851041435400205, -99.20049590361567)),
                    icon = resizedIcon,
                    onClick = {
                        selectedDrawerInfo = DrawerInfo(
                            title = "-------Taller pesado 1-------",
                            details = listOf("Control Escolar", "Finanzas", "Dirección Académica")
                        )
                        scope.launch { drawerState.open() }
                        true
                    }
                ) 

                Marker(
                    state = rememberMarkerState(geoPoint = GeoPoint(18.850149381396278, -99.2000629599035)),
                    icon = resizedIcon,
                    onClick = {
                        selectedDrawerInfo = DrawerInfo(
                            title = "-------Taller Pesado 2-------",
                            details = listOf("Control Escolar", "Finanzas", "Dirección Académica")
                        )
                        scope.launch { drawerState.open() }
                        true
                    }
                )

                Marker(
                    state = rememberMarkerState(geoPoint = GeoPoint(18.85059540899096, -99.20067127322059)),
                    icon = resizedIcon,
                    onClick = {
                        selectedDrawerInfo = DrawerInfo(
                            title = "-------Biblioteca-------",
                            details = listOf("Control Escolar", "Finanzas", "Dirección Académica")
                        )
                        scope.launch { drawerState.open() }
                        true
                    }
                ) 

                Marker(
                    state = rememberMarkerState(geoPoint = GeoPoint(18.849903028437538, -99.20127684638763)),
                    icon = resizedIcon,
                    onClick = {
                        selectedDrawerInfo = DrawerInfo(
                            title = "-------Ceviset-------",
                            details = listOf("Control Escolar", "Finanzas", "Dirección Académica")
                        )
                        scope.launch { drawerState.open() }
                        true
                    }
                )

                Marker(
                    state = rememberMarkerState(geoPoint = GeoPoint(18.849472477045325, -99.20131930266133)),
                    icon = resizedIcon,
                    onClick = {
                        selectedDrawerInfo = DrawerInfo(
                            title = "-------Cecadec-------",
                            details = listOf("Control Escolar", "Finanzas", "Dirección Académica")
                        )
                        scope.launch { drawerState.open() }
                        true
                    }
                )
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

private fun getResizedDrawable(context: Context, drawableId: Int, width: Int, height: Int): BitmapDrawable {
    val drawable = ContextCompat.getDrawable(context, drawableId)
    val bitmap = (drawable as BitmapDrawable).bitmap
    val resizedBitmap = Bitmap.createScaledBitmap(bitmap, width, height, false)
    return BitmapDrawable(context.resources, resizedBitmap)
}
