package com.aura.viewmodel.home

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aura.model.AccountResponse
import com.aura.R
import com.aura.service.HomeApiService
import com.aura.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
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
    @ApplicationContext private val context: Context,
    private val homeApi: HomeApiService,
    private val repository: Repository
) : ViewModel() {
    // Create TAG for logging
    companion object {
        private const val TAG = "HomeViewModel"
    }

    private val _accounts = MutableLiveData<List<AccountResponse>>()
    val accounts: LiveData<List<AccountResponse>> = _accounts
    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    /**
     * update uiState if there is an error in the getUserAccounts method.
     */
    private fun onError(errorMessageHome: String) {
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
                }
            } catch (e: Exception) {//handle other errors
                val errorMessageHome = context.getString(R.string.unspecified_error)
                onError(errorMessageHome)
                Log.e(TAG, errorMessageHome, e)
                _uiState.update { currentState ->
                    currentState.copy(
                        isLoading = false,
                    )
                }
            } catch (e: IOException) { //handle no internet connection exception
                val errorMessageHome = context.getString(R.string.error_no_Internet)
                onError(errorMessageHome)
                Log.e(TAG, errorMessageHome, e)
                _uiState.update { currentState ->
                    currentState.copy(
                        isLoading = false,
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



