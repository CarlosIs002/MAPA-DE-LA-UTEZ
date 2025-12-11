package com.example.gps.ui.edit

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
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.gps.viewmodel.EditPlaceViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditPlaceScreen(
    navController: NavController,
    lugarId: Int, // <-- CORREGIDO: AHORA ACEPTA LONG
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
        topBar = {
            TopAppBar(
                title = { Text("Editar Reporte") },
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
        // Si el estado inicial de carga está activo, muestra un indicador de carga en pantalla completa.
        if (uiState.nombre.isEmpty() && uiState.isLoading) {
             Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
                Text("Cargando reporte...", modifier = Modifier.padding(top = 70.dp))
            }
        } else {
            // Cuando la carga inicial termina, se muestra el formulario.
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .verticalScroll(rememberScrollState())
            ) {
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                        .weight(1f), // Ocupa el espacio disponible
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Campo de texto para Título/Edificio
                    OutlinedTextField(
                        value = uiState.nombre,
                        onValueChange = { viewModel.onNombreChange(it) },
                        label = { Text("Título o Edificio") },
                        modifier = Modifier.fillMaxWidth(),
                        isError = uiState.error?.contains("Docencia") == true, // Mantenemos la lógica de error si el backend la requiere
                        singleLine = true
                    )

                    // Campo de texto para Salón
                    OutlinedTextField(
                        value = uiState.salon,
                        onValueChange = { viewModel.onSalonChange(it) },
                        label = { Text("Aula, Salón o Laboratorio") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )

                    // Campo de texto para Descripción
                    OutlinedTextField(
                        value = uiState.descripcion,
                        onValueChange = { viewModel.onDescripcionChange(it) },
                        label = { Text("Detalles Adicionales") },
                        modifier = Modifier.fillMaxWidth().defaultMinSize(minHeight = 120.dp),
                    )

                    uiState.error?.let {
                        Text(text = it, color = MaterialTheme.colorScheme.error)
                    }
                }

                // La sección del botón se coloca al final
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shadowElevation = 8.dp // Sombra para separar visualmente
                ) {
                    Button(
                        onClick = { viewModel.guardarEdicion() },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                            .height(50.dp),
                        enabled = !uiState.isLoading
                    ) {
                        if (uiState.isLoading) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(24.dp),
                                color = MaterialTheme.colorScheme.onPrimary
                            )
                        } else {
                            Icon(Icons.Default.Save, contentDescription = "Guardar")
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Guardar Edición")
                        }
                    }
                }
            }
        }
    }
}
