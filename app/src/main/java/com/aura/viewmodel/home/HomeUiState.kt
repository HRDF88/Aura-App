package com.aura.viewmodel.home

/**
 * Ui state change class HomeActivity.
 */
data class HomeUiState(
    val isLoading: Boolean = false,
    val error: String = "",
    val retryButton: Boolean = false
)
