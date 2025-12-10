package com.example.gps.ui.edit

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.gps.viewmodel.EditPlaceViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditPlaceScreen(
    navController: NavController,
    lugarId: Int,
    viewModel: EditPlaceViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    // Cargar los datos del lugar en cuanto la pantalla se inicia
    LaunchedEffect(lugarId) {
        viewModel.cargarLugar(lugarId)
    }

    // Navegar hacia atrás cuando la edición es exitosa
    LaunchedEffect(uiState.navigateBack) {
        if (uiState.navigateBack) {
            navController.popBackStack()
            viewModel.onNavigationDone()
        }
    }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Editar Lugar") }) }
    ) {
        padding ->
        Box(modifier = Modifier.fillMaxSize().padding(padding)) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Campo de texto para Docencia/Nombre
                OutlinedTextField(
                    value = uiState.nombre,
                    onValueChange = { viewModel.onNombreChange(it) },
                    label = { Text("Docencia") },
                    modifier = Modifier.fillMaxWidth(),
                    isError = uiState.error?.contains("Docencia") == true
                )

                // Campo de texto para Salón
                OutlinedTextField(
                    value = uiState.salon,
                    onValueChange = { viewModel.onSalonChange(it) },
                    label = { Text("Salón") },
                    modifier = Modifier.fillMaxWidth()
                )

                // Campo de texto para Descripción
                OutlinedTextField(
                    value = uiState.descripcion,
                    onValueChange = { viewModel.onDescripcionChange(it) },
                    label = { Text("Descripción") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(Modifier.height(16.dp))

                Button(
                    onClick = { viewModel.guardarEdicion() },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !uiState.isLoading
                ) {
                    Text("Guardar Edición")
                }

                uiState.error?.let {
                    Text(text = it, color = MaterialTheme.colorScheme.error)
                }
            }
            if (uiState.isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
        }
    }
}