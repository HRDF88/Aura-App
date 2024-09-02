package com.aura.model

/**
 * Data class AccountResponse to create home Api response.
 */
data class AccountResponse(
    val id: String,
    val main: Boolean,
    val balance: Double,
)
