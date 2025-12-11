package com.example.gps.ui.detail

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.gps.R
import com.example.gps.data.network.RetrofitClient
import com.example.gps.viewmodel.TripDetailViewModel
import com.utsman.osmandcompose.Marker
import com.utsman.osmandcompose.OpenStreetMap
import com.utsman.osmandcompose.rememberCameraState
import com.utsman.osmandcompose.rememberMarkerState
import org.osmdroid.util.GeoPoint

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TripDetailScreen(
    navController: NavController,
    lugarId: Int, // <-- CORREGIDO
    viewModel: TripDetailViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    var showDeleteDialog by remember { mutableStateOf(false) }

    val resizedIcon = remember(context) {
        getResizedDrawable(context, R.drawable.locationpoint, 60, 60)
    }

    LaunchedEffect(lugarId) {
        if (lugarId != -1) { // <-- CORREGIDO
            viewModel.loadLugar(lugarId)
        }
    }

    LaunchedEffect(uiState.navigateBack) {
        if (uiState.navigateBack) {
            navController.popBackStack()
            viewModel.onNavigationDone() // Resetea el estado
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(uiState.lugar?.title ?: "Cargando...") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { padding ->
        Box(modifier = Modifier
            .fillMaxSize()
            .padding(padding)) {
            when {
                uiState.isLoading && uiState.lugar == null -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
                uiState.error != null -> {
                    Text(
                        text = uiState.error!!,
                        modifier = Modifier.align(Alignment.Center).padding(16.dp),
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.error
                    )
                }
                uiState.lugar != null -> {
                    val lugar = uiState.lugar!!
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                    ) {
                        val imageUrl = "${RetrofitClient.BASE_URL}${lugar.imageUrl}"
                        AsyncImage(
                            model = imageUrl,
                            contentDescription = lugar.title,
                            modifier = Modifier
                                .fillMaxWidth()
                                .aspectRatio(16 / 9f),
                            contentScale = ContentScale.Crop
                        )

                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(text = lugar.title, style = MaterialTheme.typography.headlineMedium)
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
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(250.dp),
                                shape = MaterialTheme.shapes.medium,
                                elevation = CardDefaults.cardElevation(4.dp)
                            ) {
                                val cameraState = rememberCameraState {
                                    geoPoint = GeoPoint(lugar.latitud, lugar.longitud)
                                    zoom = 17.0 // Un poco más de zoom
                                }
                                OpenStreetMap(cameraState = cameraState) {
                                    Marker(
                                        state = rememberMarkerState(geoPoint = GeoPoint(lugar.latitud, lugar.longitud)),
                                        icon = resizedIcon
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.weight(1f)) // Empuja los botones hacia abajo

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            OutlinedButton(
                                onClick = { showDeleteDialog = true },
                                modifier = Modifier.weight(1f),
                                colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.error),
                                enabled = !uiState.isLoading
                            ) {
                                Icon(Icons.Default.Delete, contentDescription = "Eliminar")
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Eliminar")
                            }

                            Button(
                                onClick = { navController.navigate("edit_place/${lugar.id}") },
                                modifier = Modifier.weight(1f),
                                enabled = !uiState.isLoading
                            ) {
                                Icon(Icons.Default.Create, contentDescription = "Editar")
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Editar")
                            }
                        }
                    }
                }
            }

            if (showDeleteDialog) {
                AlertDialog(
                    onDismissRequest = { showDeleteDialog = false },
                    title = { Text("Confirmar eliminación") },
                    text = { Text("¿Estás seguro de que deseas eliminar este lugar? Esta acción no se puede deshacer.") },
                    confirmButton = {
                        TextButton(
                            onClick = {
                                viewModel.deleteLugar()
                                showDeleteDialog = false
                            },
                            colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.error)
                        ) {
                            Text("Eliminar")
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = { showDeleteDialog = false }) {
                            Text("Cancelar")
                        }
                    }
                )
            }

            if (uiState.isLoading && uiState.lugar != null) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
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
