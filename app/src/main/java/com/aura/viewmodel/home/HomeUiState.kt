package com.aura.viewmodel.home

/**
 *
 * ui state change class homeActivity.
 */
data class HomeUiState(
    val isLoading: Boolean = false,
    val error: String = "",
    val retryButton: Boolean = false
)
