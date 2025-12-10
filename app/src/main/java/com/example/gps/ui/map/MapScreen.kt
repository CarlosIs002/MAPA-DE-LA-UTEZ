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
import com.example.gps.data.network.Lugar
import com.example.gps.viewmodel.MapViewModel
import com.utsman.osmandcompose.Marker
import com.utsman.osmandcompose.OpenStreetMap
import com.utsman.osmandcompose.rememberCameraState
import com.utsman.osmandcompose.rememberMarkerState
import kotlinx.coroutines.launch
import org.osmdroid.util.GeoPoint

@Composable
fun MapScreen(
    viewModel: MapViewModel = viewModel()
) {
    val lugares by viewModel.lugares.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    var selectedLugar by remember { mutableStateOf<Lugar?>(null) }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                selectedLugar?.let { lugar ->
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(lugar.docencia, fontWeight = FontWeight.Bold, fontSize = 20.sp)
                        Divider(modifier = Modifier.padding(vertical = 8.dp))
                        Text(lugar.description)
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
                // Iteramos sobre los lugares obtenidos de la API
                lugares.forEach { lugar ->
                    val geoPoint = GeoPoint(lugar.latitude, lugar.longitude)
                    Marker(
                        state = rememberMarkerState(geoPoint = geoPoint),
                        title = lugar.docencia,
                        onClick = {
                            selectedLugar = lugar
                            scope.launch { drawerState.open() }
                            true
                        }
                    ) {
                        // Podrías personalizar el ícono del marcador aquí
                    }
                }
            }

            // Gestión de estados de carga y error
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
