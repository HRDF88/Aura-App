package com.aura.viewmodel.login

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aura.R
import com.aura.repository.Repository
import com.aura.viewmodel.home.UserStateManager
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
import okio.IOException
import javax.inject.Inject
import javax.security.auth.login.LoginException


/**
 * LoginViewModel is responsible for preparing and managing the data for the {@link LoginActivity}.
 * It communicates with the API to fetch login details and provides
 * utility methods related to the notes UI.
 */
@HiltViewModel
class LoginViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val repository: Repository
) : ViewModel() {
    // Create TAG for logging
    companion object {
        private const val TAG = "LoginViewModel"
    }

    /**
     * Expose screen UI state.
     */
    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    /**
     * Channel to handle navigation events
     */
    private val _navigationEvents = Channel<NavigationEvent>(Channel.CONFLATED)
    val navigationEvents = _navigationEvents.receiveAsFlow()

    /**
     * Update the UI state based on user input (identifier).
     */
    fun onIdentifierChanged(identifier: String) {
        _uiState.update { currentState ->
            currentState.copy(
                identifier = identifier,
                isLoginButtonEnabled = identifier.isNotEmpty() && currentState.password.isNotEmpty()
            )

        }
    }

    /**
     * Update the UI state based on user input (password).
     */
    fun onPasswordChanged(password: String) {
        _uiState.update { currentState ->
            currentState.copy(
                password = password,
                isLoginButtonEnabled = currentState.identifier.isNotEmpty() && password.isNotEmpty()

            )
        }
    }

    /**
     * update uiState if there is an error in the method onLoginClicked.
     */
    private fun onError(errorMessage: String) {
        Log.e(TAG, errorMessage)
        _uiState.update { currentState ->
            currentState.copy(
                error = errorMessage,
                isLoading = false,
                isLoginButtonEnabled = true
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
     * Handle login button click to post identifier and password on Api.
     * @return granted to go in home_activity.
     */
    fun onLoginClicked(identifier: String, password: String) {
        viewModelScope.launch {
            _uiState.update { currentState ->
                currentState.copy(
                    isLoading = true,
                    isLoginButtonEnabled = false
                )
            }
            try {
                val response = withContext((Dispatchers.IO)) {//running in a separate thread
                    delay(1000)
                    repository.loginRequest(identifier, password)
                }
                if (response?.granted == true) {
                    UserStateManager.setUserId(identifier) // Saving User ID
                    _navigationEvents.send(NavigationEvent.NavigateToHome)
                } else {
                    val errorMessage = context.getString(R.string.error_invalid_identifier)
                    onError(errorMessage)
                }

            } catch (e: IOException) {//handle no internet connection exception
                val errorMessage = context.getString(R.string.error_no_Internet)
                onError(errorMessage)

            } catch (e: LoginException) {//handle loginException
                val errorMessage = context.getString(R.string.error_invalid_identifier)
                onError(errorMessage)

            } catch (e: Exception) {//handle other errors
                val errorMessage = context.getString(R.string.unspecified_error)
                onError(errorMessage)

            } finally {
                _uiState.update { currentState ->
                    currentState.copy(
                        isLoading = false,
                        isLoginButtonEnabled = true
                    )
                }
            }
        }
    }
}




