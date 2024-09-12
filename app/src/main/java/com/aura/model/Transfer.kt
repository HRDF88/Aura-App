package com.aura.model

/**
 * Data class to create object Transfer to use TransferApiService.
 */
data class Transfer(
    val sender: String,
    val recipient: String,
    val amount: Double
)
