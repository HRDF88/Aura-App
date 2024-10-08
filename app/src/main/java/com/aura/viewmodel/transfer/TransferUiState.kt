package com.aura.viewmodel.transfer

/**
 * Ui state change class TransferActivity.
 */
data class TransferUiState(
    val isTransferButtonEnabled: Boolean = false,
    val isLoading: Boolean = false,
    val error: String = "",
    val recipient: String ="",
    val amount: Double=0.0
)
