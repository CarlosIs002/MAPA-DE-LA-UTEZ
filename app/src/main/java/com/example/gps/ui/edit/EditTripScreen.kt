package com.example.gps.ui.edit

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.gps.viewmodel.EditTripViewModel
import com.google.android.gms.location.LocationServices

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditTripScreen(
    navController: NavController,
    photoUri: String?,
    viewModel: EditTripViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    // Set photoUri in ViewModel
    LaunchedEffect(photoUri) {
        photoUri?.let { viewModel.onImageUriChange(Uri.parse(it)) }
    }

    // Location permission handling
    val locationClient = remember { LocationServices.getFusedLocationProviderClient(context) }
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { granted ->
            if (granted) {
                if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    locationClient.lastLocation.addOnSuccessListener { location ->
                        viewModel.onLocationChange(location)
                    }
                }
            }
        }
    )

    LaunchedEffect(Unit) {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            launcher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        } else {
             locationClient.lastLocation.addOnSuccessListener { location ->
                viewModel.onLocationChange(location)
            }
        }
    }
    
    // Navigate back when done
    LaunchedEffect(uiState.navigateBack) {
        if (uiState.navigateBack) {
            navController.popBackStack()
            viewModel.onNavigationDone() // Reset state
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Añadir Detalles del Reporte") },
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            // Scrollable content area for the form
            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Image preview
                uiState.imageUri?.let {
                    AsyncImage(
                        model = it,
                        contentDescription = "Foto del reporte",
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(16 / 9f)
                            .clip(MaterialTheme.shapes.medium),
                        contentScale = ContentScale.Crop
                    )
                }

                // Text fields
                OutlinedTextField(
                    value = uiState.titulo,
                    onValueChange = { viewModel.onTituloChange(it) },
                    label = { Text("Título o Edificio") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    isError = uiState.error?.contains("Título") == true
                )

                OutlinedTextField(
                    value = uiState.salon,
                    onValueChange = { viewModel.onSalonChange(it) },
                    label = { Text("Aula o Laboratorio (Opcional)") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                OutlinedTextField(
                    value = uiState.descripcion,
                    onValueChange = { viewModel.onDescripcionChange(it) },
                    label = { Text("Descripción detallada del reporte") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .defaultMinSize(minHeight = 120.dp),
                    isError = uiState.error?.contains("Descripción") == true
                )

                // Display error message
                uiState.error?.let {
                    Text(it, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
                }

                // Display a message while fetching location
                if (uiState.currentLocation == null && !uiState.isLoading) {
                     Row(verticalAlignment = Alignment.CenterVertically) {
                        CircularProgressIndicator(modifier = Modifier.size(20.dp))
                        Spacer(Modifier.width(8.dp))
                        Text("Obteniendo ubicación...", style = MaterialTheme.typography.bodySmall)
                    }
                }
            }

            // Bottom bar for the save button
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shadowElevation = 8.dp
            ) {
                Button(
                    onClick = { viewModel.saveTrip() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .height(50.dp),
                    // Disable button while loading or if location is not yet available
                    enabled = !uiState.isLoading && uiState.currentLocation != null
                ) {
                    if (uiState.isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    } else {
                        Icon(Icons.Default.Save, contentDescription = "Guardar Reporte")
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Guardar Reporte")
                    }
                }
            }
        }
    }
}
