package com.aura.viewmodel.transfer

import android.content.Context
import androidx.lifecycle.ViewModel
import com.aura.repository.Repository
import com.aura.service.TransferApiService
import com.aura.viewmodel.home.HomeUiState
import com.aura.viewmodel.login.NavigationEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class TransferViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val transferApi: TransferApiService,
    private val repository: Repository
) : ViewModel() {
    companion object {
        private const val TAG = "TransferViewModel"
    }

    /**
     * Expose screen UI state.
     */
    private val _uiState = MutableStateFlow(TransferUiState())
    val uiState: StateFlow<TransferUiState> = _uiState.asStateFlow()

    /**
     * Channel to handle navigation events
     */
    private val _navigationEvents = Channel<NavigationEvent>(Channel.CONFLATED)
    val navigationEvents = _navigationEvents.receiveAsFlow()

    /**
     * Update the UI state based on user input (recipient).
     */
    fun onRecipientChanged(recipient: String) {
        _uiState.update { currentState ->
            currentState.copy(
               recipient = recipient,
                isTransferButtonEnabled = currentState.recipient.isNotEmpty() && currentState.amount.toString().isNotEmpty()
            )

        }
    }
}