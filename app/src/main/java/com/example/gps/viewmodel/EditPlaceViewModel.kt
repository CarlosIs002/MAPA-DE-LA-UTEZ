package com.example.gps.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.gps.data.repository.LugarRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.IOException

// Data class para mantener el estado de la pantalla de edición
data class EditPlaceUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val navigateBack: Boolean = false,
    val nombre: String = "",
    val salon: String = "",
    val descripcion: String = ""
)

class EditPlaceViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = LugarRepository(application)

    private val _uiState = MutableStateFlow(EditPlaceUiState())
    val uiState = _uiState.asStateFlow()

    private var lugarId: Int? = null

    // Carga los datos iniciales del lugar que se va a editar
    fun cargarLugar(id: Int) {
        lugarId = id
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            try {
                val lugar = repository.getLugar(id)
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        nombre = lugar.title,
                        salon = lugar.salon ?: "",
                        descripcion = lugar.descripcion ?: ""
                    )
                }
            } catch (e: IOException) {
                _uiState.update { it.copy(isLoading = false, error = "Error de red al cargar el lugar.") }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, error = "Error inesperado: ${e.message}") }
            }
        }
    }

    // Funciones para actualizar el estado con cada cambio en los campos de texto
    fun onNombreChange(nuevoNombre: String) {
        _uiState.update { it.copy(nombre = nuevoNombre) }
    }

    fun onSalonChange(nuevoSalon: String) {
        _uiState.update { it.copy(salon = nuevoSalon) }
    }

    fun onDescripcionChange(nuevaDescripcion: String) {
        _uiState.update { it.copy(descripcion = nuevaDescripcion) }
    }

    // Guarda los cambios realizados
    fun guardarEdicion() {
        val currentId = lugarId ?: return // No hacer nada si el ID es nulo
        val currentState = _uiState.value

        if (currentState.nombre.isBlank()) {
             _uiState.update { it.copy(error = "El campo 'Docencia' no puede estar vacío.") }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            try {
                repository.actualizarLugar(
                    id = currentId,
                    nombre = currentState.nombre,
                    salon = currentState.salon,
                    descripcion = currentState.descripcion
                )
                _uiState.update { it.copy(isLoading = false, navigateBack = true) }
            } catch (e: IOException) {
                _uiState.update { it.copy(isLoading = false, error = "Error de red al guardar.") }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, error = "Error inesperado al guardar: ${e.message}") }
            }
        }
    }
    
    fun onNavigationDone(){
        _uiState.update { it.copy(navigateBack = false) }
    }
}