package com.resse.notesapp.data.uistate

sealed class UiState {
    object Loading : UiState()
    object Success: UiState()
    object Error : UiState()
}