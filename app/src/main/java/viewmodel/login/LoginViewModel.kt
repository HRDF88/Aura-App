package viewmodel.login

import android.content.Context
import android.util.Log
import androidx.core.content.ContextCompat.getString
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aura.Bo.User
import com.aura.R
import com.aura.Utils.LoginApiService
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import okio.IOException
import javax.inject.Inject
import javax.security.auth.login.LoginException


/**
 * LoginViewModel is responsible for preparing and managing the data for the {@link LoginActivity}.
 * It communicates with the API to fetch login details and provides
 * utility methods related to the notes UI.
 */
@HiltViewModel
class LoginViewModel @Inject constructor(@ApplicationContext private val context: Context, private val loginApi: LoginApiService) : ViewModel() {
    // Create TAG for logging
    companion object {
        private const val TAG = "LoginViewModel"
    }

    // Expose screen UI state
    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    // Channel to handle navigation events
    private val _navigationEvents = Channel<NavigationEvent>(Channel.CONFLATED)
    val navigationEvents = _navigationEvents.receiveAsFlow()

    // Update the UI state based on user input
    fun onIdentifierChanged(identifier: String) {
        _uiState.update { currentState ->
            currentState.copy(
                identifier = identifier,
                isLoginButtonEnabled = identifier.isNotEmpty() && currentState.password.isNotEmpty()
            )

        }
    }

    fun onPasswordChanged(password: String) {
        _uiState.update { currentState ->
            currentState.copy(
                password = password,
                isLoginButtonEnabled = currentState.identifier.isNotEmpty() && password.isNotEmpty()

            )
        }
    }

    fun onError(errorMessage:String){
        _uiState.update { currentState->
            currentState.copy(
                error = errorMessage
            )
        }
    }

    // Handle login button click
    fun onLoginClicked(identifier: String, password: String) {
        viewModelScope.launch {
            _uiState.update { currentState ->
                currentState.copy(
                    isLoading = true,
                    isLoginButtonEnabled = false

                )
            }

            _uiState.update { currentState ->
                currentState.copy(
                    isLoading = false,
                    isLoginButtonEnabled = true
                )
            }
            try {
                val user = User(identifier, password)
                val response = loginApi.postLogin(user)


                if (response.isSuccessful) {
                    _navigationEvents.send(NavigationEvent.NavigateToHome)
                }
                else{
                    val errorMessage = "Erreur de connexion"
                    onError(errorMessage)
                    Log.e(TAG,errorMessage)
                }

            } catch (e: IOException) {
                //gérer l'exception d'absence de connexion Internet'
                val errorMessage = context.getString(R.string.error_no_Internet)
                onError(errorMessage)
                Log.e(TAG, errorMessage, e)
                _uiState.update { currentState ->
                    currentState.copy(
                        isLoading = false,
                        isLoginButtonEnabled = true


                    )
                }

            } catch (e: LoginException) {
                //gérer ereur indentifiants incorects
                val errorMessage = context.getString(R.string.error_invalid_identifier)
                onError(errorMessage)
                Log.e(TAG, errorMessage, e)
                _uiState.update { currentState ->
                    currentState.copy(
                        isLoading = false,
                        isLoginButtonEnabled = true

                    )
                }
            } catch (e: Exception) {
                //gérer autres erreurs
                val errorMessage = context.getString(R.string.unspecified_error)
                onError(errorMessage)
                Log.e(TAG, errorMessage, e)
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




