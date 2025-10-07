package com.example.ref01.domain.location

import com.example.ref01.data.location.LocationAddress

sealed interface LocationUiState {
    data object Idle : LocationUiState
    data object Loading : LocationUiState
    data class Success(val data: LocationAddress) : LocationUiState
    data class Error(val message: String) : LocationUiState
}
