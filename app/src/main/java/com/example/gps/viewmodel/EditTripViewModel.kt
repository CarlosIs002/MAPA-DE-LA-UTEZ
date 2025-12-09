package com.example.gps.viewmodel

import android.app.Application
import android.location.Location
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.gps.data.repository.LugarRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.IOException

// Estado simplificado solo para la creación
data class EditTripUiState(
    val titulo: String = "",
    val descripcion: String = "",
    val imageUri: Uri? = null,
    val currentLocation: Location? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
    val navigateBack: Boolean = false
)

class EditTripViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = LugarRepository(application)

    private val _uiState = MutableStateFlow(EditTripUiState())
    val uiState = _uiState.asStateFlow()

    fun onTituloChange(newTitulo: String) {
        _uiState.update { it.copy(titulo = newTitulo) }
    }

    fun onDescripcionChange(newDescripcion: String) {
        _uiState.update { it.copy(descripcion = newDescripcion) }
    }

    fun onImageUriChange(newUri: Uri?) {
        _uiState.update { it.copy(imageUri = newUri) }
    }

    fun onLocationChange(newLocation: Location?) {
        _uiState.update { it.copy(currentLocation = newLocation) }
    }

    // La función solo sabe crear un registro
    fun saveTrip() {
        val currentState = _uiState.value
        val uri = currentState.imageUri
        val location = currentState.currentLocation

        if (currentState.titulo.isBlank() || uri == null || location == null) {
            _uiState.update { it.copy(error = "El título, la imagen y la ubicación son obligatorios.") }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            try {
                repository.createLugar(
                    nombre = currentState.titulo,
                    descripcion = currentState.descripcion,
                    imageUri = uri,
                    latitud = location.latitude,
                    longitud = location.longitude
                )
                _uiState.update { it.copy(isLoading = false, navigateBack = true) }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, error = "Error al crear el lugar: ${e.message}") }
            }
        }
    }

    fun onNavigationDone() {
        _uiState.update { it.copy(navigateBack = false) }
    }
}