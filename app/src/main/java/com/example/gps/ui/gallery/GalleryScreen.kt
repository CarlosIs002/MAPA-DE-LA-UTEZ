package com.example.gps.ui.gallery

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.gps.data.network.Lugar
import com.example.gps.viewmodel.GalleryViewModel

private const val BASE_URL = "http://192.168.107.157:5000"

@Composable
fun GalleryScreen(
    navController: NavController,
    viewModel: GalleryViewModel = viewModel()
) {
    val lugares by viewModel.lugares.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()
    val lifecycleOwner = LocalLifecycleOwner.current

    // --- SOLUCIÓN DE REFRESCO AUTOMÁTICO ---
    // DisposableEffect se asegura de que el observador se añada y se quite correctamente.
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            // Si el evento es ON_RESUME, la pantalla se ha vuelto a mostrar.
            if (event == Lifecycle.Event.ON_RESUME) {
                // Le pedimos al ViewModel que vuelva a cargar los lugares.
                viewModel.loadLugares()
            }
        }

        // Añadimos el observador al ciclo de vida.
        lifecycleOwner.lifecycle.addObserver(observer)

        // onDispose se llama cuando la pantalla se va a destruir.
        onDispose {
            // Quitamos el observador para evitar fugas de memoria.
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    Scaffold {
        padding ->
        Box(modifier = Modifier
            .fillMaxSize()
            .padding(padding)) {
            when {
                // Muestra el indicador de carga solo si la lista está vacía al principio.
                isLoading && lugares.isEmpty() -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
                error != null -> {
                    Text(
                        text = error!!,
                        modifier = Modifier.align(Alignment.Center),
                        textAlign = TextAlign.Center
                    )
                }
                lugares.isEmpty() -> {
                    Text(
                        text = "No hay lugares guardados.",
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                else -> {
                    LazyVerticalGrid(
                        columns = GridCells.Adaptive(minSize = 128.dp),
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        items(lugares) { lugar ->
                            LugarGridItem(navController, lugar)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun LugarGridItem(navController: NavController, lugar: Lugar) {
    Card(
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        modifier = Modifier
            .aspectRatio(1f)
            .clickable { navController.navigate("lugar_detail/${lugar.id}") }
    ) {
        val imageUrl = "$BASE_URL${lugar.imageUrl}"

        AsyncImage(
            model = imageUrl,
            contentDescription = lugar.docencia,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
    }
}
