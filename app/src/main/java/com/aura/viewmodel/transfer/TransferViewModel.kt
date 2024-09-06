package com.aura.viewmodel.transfer

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aura.R
import com.aura.repository.Repository
import com.aura.viewmodel.login.NavigationEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException
import javax.inject.Inject

/**
 * TransferViewModel is responsible for preparing and managing the data for the {@link TransferActivity}.
 * It communicates with the API to fetch login details and provides
 * utility methods related to the notes UI.
 */
@HiltViewModel
class TransferViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val repository: Repository,


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


    /**
     * Update the UI state based on user input (recipient).
     */
    fun onRecipientChanged(recipient: String) {
        _uiState.update { currentState ->
            currentState.copy(
                recipient = recipient,
                isTransferButtonEnabled = currentState.recipient.isNotEmpty() && currentState.amount > 0.0
            )
        }
    }

    /**
     * Update the UI state based on user input (amount).
     */
    fun onAmountChanged(amount: Double) {
        _uiState.update { currentState ->
            currentState.copy(
                amount = amount,
                isTransferButtonEnabled = currentState.recipient.isNotEmpty() && currentState.amount > 0.0
            )
        }
    }

    /**
     * update uiState if there is an error in the method onTransferClicked.
     */
    private fun onError(errorMessage: String) {
        Log.e(TAG, errorMessage)
        _uiState.update { currentState ->
            currentState.copy(
                error = errorMessage,
                isLoading = false,
                isTransferButtonEnabled = true
            )
        }
    }

    /**
     * Update error state to reset its value after the error message is broadcast.
     */
    fun updateErrorState(errorMessage: String) {
        val currentState = uiState.value
        val updatedState = currentState.copy(error = errorMessage)
        _uiState.value = updatedState
    }

    /**
     * Handle transfer button click to post sender, recipient and amount on Api.
     * @return result to go in transfer_activity.
     */
    fun onTransferClicked(recipient: String, amount: Double) {
        viewModelScope.launch {
            _uiState.update { currentState ->
                currentState.copy(
                    isLoading = true,
                    isTransferButtonEnabled = false
                )
            }
            try {
                val response = withContext(Dispatchers.IO) {
                    delay(1000)
                    repository.transferRequest(recipient, amount)
                }
                if (response != null) {
                    if (response.result == true) {
                        _navigationEvents.send(NavigationEvent.NavigateToHome)
                    } else {
                        val errorMessage = context.getString(R.string.insufficient_balance_amount)
                        onError(errorMessage)
                    }
                } else {
                    val errorMessage = context.getString(R.string.error_transfer)
                    onError(errorMessage)
                }
            } catch (e: IOException) {
                val errorMessage = context.getString(R.string.error_no_Internet)
                onError(errorMessage)
            } catch (e: Exception) {
                val errorMessage = context.getString(R.string.unspecified_error)
                onError(errorMessage)
            } finally {
                _uiState.update { currentState ->
                    currentState.copy(
                        isLoading = false,
                        isTransferButtonEnabled = true
                    )
                }
            }
        }
    }
}