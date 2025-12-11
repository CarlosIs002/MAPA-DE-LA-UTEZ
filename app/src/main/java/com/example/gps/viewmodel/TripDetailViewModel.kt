package com.example.gps.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.gps.data.network.Lugar
import com.example.gps.data.repository.LugarRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.IOException

data class LugarDetailState(
    val lugar: Lugar? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
    val navigateBack: Boolean = false
)

class TripDetailViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = LugarRepository(application)

    private val _uiState = MutableStateFlow(LugarDetailState())
    val uiState = _uiState.asStateFlow()

    fun loadLugar(lugarId: Int) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            try {
                val lugar = repository.getLugar(lugarId)
                _uiState.update { it.copy(isLoading = false, lugar = lugar) }
            } catch (e: IOException) {
                _uiState.update { it.copy(isLoading = false, error = "Error de red. No se pudo cargar el lugar.") }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, error = "Error inesperado: ${e.message}") }
            }
        }
    }

    fun deleteLugar() {
        _uiState.value.lugar?.id?.let { id ->
            viewModelScope.launch {
                _uiState.update { it.copy(isLoading = true, error = null) }
                try {
                    repository.deleteLugar(id)
                    _uiState.update { it.copy(isLoading = false, navigateBack = true) } // ¡Éxito!
                } catch (e: IOException) {
                    _uiState.update { it.copy(isLoading = false, error = "Error de red. No se pudo eliminar el lugar.") }
                } catch (e: Exception) {
                    _uiState.update { it.copy(isLoading = false, error = "Error inesperado: ${e.message}") }
                }
            }
        }
    }
    
    fun onNavigationDone() {
        _uiState.update { it.copy(navigateBack = false) }
    }
}
