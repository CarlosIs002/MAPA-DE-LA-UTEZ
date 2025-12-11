package com.example.gps.ui.map

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.gps.R
import com.example.gps.viewmodel.MapViewModel
import com.utsman.osmandcompose.Marker
import com.utsman.osmandcompose.OpenStreetMap
import com.utsman.osmandcompose.rememberCameraState
import com.utsman.osmandcompose.rememberMarkerState
import kotlinx.coroutines.launch
import org.osmdroid.util.GeoPoint

data class FixedMarker(val geoPoint: GeoPoint, val title: String, val details: List<String>)

data class SelectedMarkerData(
    val id: Int? = null,
    val title: String,
    val details: List<String>,
    val isFromDb: Boolean
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapScreen(
    navController: NavController,
    viewModel: MapViewModel = viewModel()
) {
    val lugares by viewModel.lugares.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()
    val context = LocalContext.current

    val resizedIcon = remember(context) {
        getResizedDrawable(context, R.drawable.locationpoint, 60, 60)
    }

    val fixedMarkers = remember {
        listOf(
            FixedMarker(GeoPoint(18.850454, -99.201372), "Rectoria", listOf("Control Escolar", "Finanzas", "Dirección Académica")),
            FixedMarker(GeoPoint(18.852418, -99.200156), "Docencia 4", listOf("Salones de clase")),
            FixedMarker(GeoPoint(18.852115, -99.200133), "Docencia 3", listOf("Salones de clase")),
            FixedMarker(GeoPoint(18.852467, -99.200729), "CEDIM", listOf("Centro de Desarrollo e Innovación")),
            FixedMarker(GeoPoint(18.852144, -99.200995), "Docencia 5", listOf("Salones de clase")),
            FixedMarker(GeoPoint(18.851806, -99.200945), "Docencia 2", listOf("Laboratorio de Redes", "Laboratorio de Software", "Salón 201")),
            FixedMarker(GeoPoint(18.851396, -99.200873), "Docencia 1", listOf("Salón 101", "Salón 102", "Salón 103", "Auditorio")),
            FixedMarker(GeoPoint(18.851041, -99.200495), "Taller Pesado 1", listOf("Talleres y laboratorios")),
            FixedMarker(GeoPoint(18.850149, -99.200062), "Taller Pesado 2", listOf("Talleres y laboratorios")),
            FixedMarker(GeoPoint(18.850595, -99.200671), "Biblioteca", listOf("Préstamo de libros", "Sala de estudio")),
            FixedMarker(GeoPoint(18.849903, -99.201276), "CEVISET", listOf("Centro de Vinculación y Servicios Tecnológicos")),
            FixedMarker(GeoPoint(18.849472, -99.201319), "CECADEC", listOf("Centro de Capacitación y Desarrollo"))
        )
    }

    var selectedMarkerInfo by remember { mutableStateOf<SelectedMarkerData?>(null) }
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()

    // Efecto para limpiar el estado de forma segura cuando la hoja se oculta
    LaunchedEffect(sheetState.isVisible) {
        if (!sheetState.isVisible) {
            selectedMarkerInfo = null
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mapa de la UTEZ") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { paddingValues ->
        val cameraState = rememberCameraState {
            geoPoint = GeoPoint(18.851252, -99.201060) // UTEZ
            zoom = 18.8
        }

        Box(modifier = Modifier.fillMaxSize().padding(paddingValues)) {
            OpenStreetMap(
                modifier = Modifier.fillMaxSize(),
                cameraState = cameraState
            ) {
                lugares.forEach { lugar ->
                    Marker(
                        state = rememberMarkerState(geoPoint = GeoPoint(lugar.latitud, lugar.longitud)),
                        title = lugar.title,
                        icon = resizedIcon,
                        onClick = { _ ->
                            selectedMarkerInfo = SelectedMarkerData(
                                id = lugar.id,
                                title = lugar.title,
                                details = listOfNotNull(lugar.salon, lugar.descripcion).filter { it.isNotBlank() },
                                isFromDb = true
                            )
                            scope.launch { sheetState.show() }
                            true
                        }
                    )
                }

                fixedMarkers.forEach { marker ->
                    Marker(
                        state = rememberMarkerState(geoPoint = marker.geoPoint),
                        icon = resizedIcon,
                        title = marker.title,
                        onClick = { _ ->
                             selectedMarkerInfo = SelectedMarkerData(
                                title = marker.title,
                                details = marker.details,
                                isFromDb = false
                            )
                            scope.launch { sheetState.show() }
                            true
                        }
                    )
                }
            }

            // Copia local y estable de la información del marcador seleccionado
            val currentInfo = selectedMarkerInfo
            if (currentInfo != null) {
                ModalBottomSheet(
                    onDismissRequest = { scope.launch { sheetState.hide() } },
                    sheetState = sheetState
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = currentInfo.title,
                            style = MaterialTheme.typography.headlineSmall,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Divider()
                    }
                    LazyColumn(
                        modifier = Modifier.padding(horizontal = 16.dp)
                    ) {
                        items(currentInfo.details) { detail ->
                            ListItem(
                                headlineContent = { Text(detail) }
                            )
                        }
                    }

                    if (currentInfo.isFromDb) {
                        Button(
                            onClick = { 
                                scope.launch { sheetState.hide() }.invokeOnCompletion {
                                    currentInfo.id?.let { navController.navigate("lugar_detail/$it") }
                                }
                             },
                            modifier = Modifier.fillMaxWidth().padding(16.dp)
                        ) {
                            Text("Ver Detalles y Editar")
                            Spacer(modifier = Modifier.width(8.dp))
                            Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = "Ver Detalles")
                        }
                    }
                    Spacer(modifier = Modifier.height(32.dp))
                }
            }

            when {
                isLoading -> CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                error != null -> Text(
                    text = error!!,
                    modifier = Modifier.align(Alignment.Center).padding(16.dp),
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.error
                )
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
