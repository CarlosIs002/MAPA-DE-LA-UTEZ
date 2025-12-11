package com.example.gps.ui.map

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
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

data class FixedMarker(val geoPoint: GeoPoint, val title: String, val details: List<String>)

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
        getResizedDrawable(context, R.drawable.locationpoint, 60, 60)
    }

    val fixedMarkers = remember {
        listOf(
            FixedMarker(GeoPoint(18.85045408195868, -99.20137222324546), "Rectoria", listOf("Control Escolar", "Finanzas", "Dirección Académica")),
            FixedMarker(GeoPoint(18.85139623679503, -99.20087307973316), "Docencia 1", listOf("Salón 101", "Salón 102", "Salón 103", "Auditorio")),
            FixedMarker(GeoPoint(18.852418469088246, -99.20015652703296), "Docencia 4", listOf("Salones de clase")),
            FixedMarker(GeoPoint(18.852115607339066, -99.20013329906479), "Docencia 3", listOf("Salones de clase")),
            FixedMarker(GeoPoint(18.852144916564487, -99.2009953147721), "Docencia 5", listOf("Salones de clase")),
            FixedMarker(GeoPoint(18.851806567244157, -99.20094534452299), "Docencia 2", listOf("Laboratorio de Redes", "Laboratorio de Software", "Salón 201")),
            FixedMarker(GeoPoint(18.852467317706274, -99.20072948358094), "CEDIM", listOf("Centro de Desarrollo e Innovación")),
            FixedMarker(GeoPoint(18.851041435400205, -99.20049590361567), "Taller Pesado 1", listOf("Talleres y laboratorios")),
            FixedMarker(GeoPoint(18.850149381396278, -99.2000629599035), "Taller Pesado 2", listOf("Talleres y laboratorios")),
            FixedMarker(GeoPoint(18.85059540899096, -99.20067127322059), "Biblioteca", listOf("Préstamo de libros", "Sala de estudio")),
            FixedMarker(GeoPoint(18.849903028437538, -99.20127684638763), "CEVISET", listOf("Centro de Vinculación y Servicios Tecnológicos")),
            FixedMarker(GeoPoint(18.849472477045325, -99.20131930266133), "CECADEC", listOf("Centro de Capacitación y Desarrollo")),
        )
    }

    var selectedMarkerInfo by remember { mutableStateOf<Pair<String, List<String>>?>(null) }
    val bottomSheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()

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
            zoom = 18.0
        }

        Box(modifier = Modifier.fillMaxSize().padding(paddingValues)) {
            OpenStreetMap(
                modifier = Modifier.fillMaxSize(),
                cameraState = cameraState
            ) {
                // Marcadores de la base de datos
                lugares.forEach { lugar ->
                    val locationPoint = GeoPoint(lugar.latitud, lugar.longitud)
                    Marker(
                        state = rememberMarkerState(geoPoint = locationPoint),
                        title = lugar.title,
                        icon = resizedIcon,
                        onClick = {
                            selectedMarkerInfo = lugar.title to listOfNotNull(lugar.salon, lugar.descripcion).filter { it.isNotBlank() }
                            scope.launch { bottomSheetState.show() }
                            true
                        }
                    )
                }

                // Marcadores fijos
                fixedMarkers.forEach { marker ->
                    Marker(
                        state = rememberMarkerState(geoPoint = marker.geoPoint),
                        icon = resizedIcon,
                        title = marker.title,
                        onClick = {
                            selectedMarkerInfo = marker.title to marker.details
                            scope.launch { bottomSheetState.show() }
                            true
                        }
                    )
                }
            }

            if (selectedMarkerInfo != null) {
                ModalBottomSheet(
                    onDismissRequest = { selectedMarkerInfo = null },
                    sheetState = bottomSheetState
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = selectedMarkerInfo!!.first,
                            style = MaterialTheme.typography.headlineSmall,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Divider()
                    }
                    LazyColumn(
                        modifier = Modifier.padding(horizontal = 16.dp)
                    ) {
                        items(selectedMarkerInfo!!.second) { detail ->
                            ListItem(
                                headlineContent = { Text(detail) }
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(32.dp))
                }
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
                        color = MaterialTheme.colorScheme.error
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
