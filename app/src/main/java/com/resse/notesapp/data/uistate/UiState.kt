package com.resse.notesapp.data.uistate

sealed class UiState {
    object Loading : UiState()
    object Success: UiState()
    class Error(val text: String) : UiState()
}