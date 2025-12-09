package com.example.gps.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.gps.data.network.Lugar
import com.example.gps.data.repository.LugarRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.IOException

class GalleryViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = LugarRepository(application)

    private val _lugares = MutableStateFlow<List<Lugar>>(emptyList())
    val lugares: StateFlow<List<Lugar>> = _lugares.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    // El bloque init llama a la función pública
    init {
        loadLugares()
    }

    // La función ahora es pública y tiene el nombre correcto para ser llamada desde la UI
    fun loadLugares() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                _lugares.value = repository.getLugares()
            } catch (e: IOException) {
                _error.value = "No se pudo conectar al servidor. ¿Estás conectado a la red?"
            } catch (e: Exception) {
                _error.value = "Ocurrió un error inesperado: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }
}