package com.example.gps.ui.detail

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.gps.viewmodel.TripDetailViewModel
import com.utsman.osmandcompose.Marker
import com.utsman.osmandcompose.OpenStreetMap
import com.utsman.osmandcompose.rememberCameraState
import com.utsman.osmandcompose.rememberMarkerState
import org.osmdroid.util.GeoPoint

private const val BASE_URL = "http://192.168.110.42:5000/"

@Composable
fun TripDetailScreen(
    navController: NavController,
    lugarId: Long,
    viewModel: TripDetailViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(lugarId) {
        if (lugarId != -1L) {
            viewModel.loadLugar(lugarId.toInt())
        }
    }

    LaunchedEffect(uiState.navigateBack) {
        if (uiState.navigateBack) {
            navController.popBackStack()
        }
    }

    Scaffold {
            padding ->
        Box(modifier = Modifier.fillMaxSize().padding(padding)) {
            when {
                uiState.isLoading && uiState.lugar == null -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
                uiState.error != null -> {
                    Text(
                        text = uiState.error!!,
                        modifier = Modifier.align(Alignment.Center),
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.error
                    )
                }
                uiState.lugar != null -> {
                    val lugar = uiState.lugar!!
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp)
                            .verticalScroll(rememberScrollState())
                    ) {
                        val imageUrl = "$BASE_URL${lugar.imageUrl}"
                        AsyncImage(
                            model = imageUrl,
                            contentDescription = lugar.title,
                            modifier = Modifier
                                .fillMaxWidth()
                                .aspectRatio(16 / 9f)
                                .clip(MaterialTheme.shapes.medium),
                            contentScale = ContentScale.Crop
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Text(text = lugar.title, style = MaterialTheme.typography.headlineLarge)
                        Spacer(modifier = Modifier.height(4.dp))

                        if (!lugar.salon.isNullOrBlank()) {
                            Text(
                                text = "Salón: ${lugar.salon}",
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.secondary
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                        }

                        if (!lugar.descripcion.isNullOrBlank()) {
                            Text(text = lugar.descripcion, style = MaterialTheme.typography.bodyLarge)
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Card(
                            modifier = Modifier.fillMaxWidth().height(200.dp),
                            elevation = CardDefaults.cardElevation(4.dp)
                        ) {
                            val cameraState = rememberCameraState {
                                geoPoint = GeoPoint(lugar.latitud, lugar.longitud)
                                zoom = 16.0
                            }
                            OpenStreetMap(cameraState = cameraState) {
                                Marker(
                                    state = rememberMarkerState(geoPoint = GeoPoint(lugar.latitud, lugar.longitud))
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(32.dp))

                        Button(
                            onClick = { viewModel.deleteLugar() },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error),
                            enabled = !uiState.isLoading
                        ) {
                            if (uiState.isLoading) {
                                CircularProgressIndicator(color = MaterialTheme.colorScheme.onError)
                            } else {
                                Icon(Icons.Default.Delete, contentDescription = "Eliminar")
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Eliminar")
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        // --- BOTÓN EDITAR ACTIVADO ---
                        Button(
                            onClick = { navController.navigate("edit_place/${lugar.id}") },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(71, 89, 248, 255)),
                            enabled = !uiState.isLoading
                        ) {
                            if (uiState.isLoading) {
                                CircularProgressIndicator(color = MaterialTheme.colorScheme.onError)
                            } else {
                                Icon(Icons.Default.Create, contentDescription = "Editar")
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Editar")
                            }
                        }
                    }
                }
            }
        }
    }
}
