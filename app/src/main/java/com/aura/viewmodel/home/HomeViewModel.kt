package com.aura.viewmodel.home

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aura.model.AccountResponse
import com.aura.R
import com.aura.repository.Repository
import com.aura.viewmodel.login.LoginViewModel
import com.aura.viewmodel.login.NavigationEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okio.IOException
import javax.inject.Inject

/**
 * HomeViewModel is responsible for preparing and managing the data for the {@link HomeActivity}.
 * It communicates with the Repository to fetch account's user details and provides
 * utility methods related to the notes UI.
 */
@HiltViewModel
class HomeViewModel @Inject constructor(
    @SuppressLint("StaticFieldLeak") @ApplicationContext private val context: Context,
    private val repository: Repository
) : ViewModel() {
    // Create TAG for logging
    companion object {
        private const val TAG = "HomeViewModel"
    }

    private val _accounts = MutableLiveData<List<AccountResponse>>()
    val accounts: LiveData<List<AccountResponse>> = _accounts

    /**
     * Expose screen UI state.
     */
    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    /**
     * Channel to handle navigation events
     */
    private val _navigationEvents = Channel<NavigationEvent>(Channel.CONFLATED)
    /**
     * update uiState if there is an error in the getUserAccounts method.
     */
    private fun onError(errorMessageHome: String) {
        Log.e(TAG, errorMessageHome)
        _uiState.update { currentState ->
            currentState.copy(
                error = errorMessageHome,
                isLoading = false,
                retryButton = true,
            )
        }
    }

    /**
     * Update error state to reset its value after the error message is broadcast.
     */
    fun updateErrorState(errorMessageHome: String) {
        val currentState = uiState.value
        val updatedState = currentState.copy(error = errorMessageHome)
        _uiState.value = updatedState

    }

    /**
     * Update retryButton state to reset its value after the retry button iss clicked.
     */
    fun updateRetryButtonState(retry:Boolean) {
        val currentState = uiState.value
        val updatedState = currentState.copy(retryButton = retry)
        _uiState.value = updatedState
    }
    /**
     * Method that retrieves the user's balance from the repository, also allows network error handling
     * and other exceptions.
     */
    fun getUserAccounts() {
        viewModelScope.launch {
            _uiState.update { currentState ->
                currentState.copy(
                    isLoading = true
                )
            }
            try {
                val accounts = withContext(Dispatchers.IO) {//running in a separate thread
                    delay(1000)
                    repository.getUserAccounts()//call the repository with méthod getAccounts to catch accounts of user
                }
                _accounts.value = accounts
                if (accounts.isEmpty()) {
                    onError(errorMessageHome = context.getString(R.string.error_get_account))
                    _uiState.update { currentState ->
                        currentState.copy(
                            isLoading = false,
                            retryButton = true)
                    }

                }
            } catch (e: Exception) {//handle other errors
                val errorMessageHome = context.getString(R.string.unspecified_error)
                onError(errorMessageHome)
                Log.e(TAG, errorMessageHome, e)
                _uiState.update { currentState ->
                    currentState.copy(
                        isLoading = false,
                        retryButton = true
                    )
                }
            } catch (e: IOException) { //handle no internet connection exception
                val errorMessageHome = context.getString(R.string.error_no_Internet)
                onError(errorMessageHome)
                Log.e(TAG, errorMessageHome, e)
                _uiState.update { currentState ->
                    currentState.copy(
                        isLoading = false,
                        retryButton = true
                    )
                }
            } finally {
                _uiState.update { currentState ->
                    currentState.copy(//refresh the uiState by setting isLoading false
                        isLoading = false,
                    )
                }
            }
        }
    }
}



