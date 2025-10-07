package com.example.ref01.ui.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.ref01.data.location.LocationRepository
import com.example.ref01.domain.location.LocationUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class LocationViewModel(
    app: Application,
    // Inyección por constructor con valor por defecto (producción)
    private val repo: LocationRepository = LocationRepository(app.applicationContext)
) : AndroidViewModel(app) {

    private val _state = MutableStateFlow<LocationUiState>(LocationUiState.Idle)
    val state: StateFlow<LocationUiState> = _state

    fun fetchLocation() {
        _state.value = LocationUiState.Loading
        viewModelScope.launch {
            val result = runCatching { repo.getCurrentLocation() }
            _state.value = result.getOrNull()?.let { LocationUiState.Success(it) }
                ?: LocationUiState.Error("No fue posible obtener la ubicación.")
        }
    }
}
