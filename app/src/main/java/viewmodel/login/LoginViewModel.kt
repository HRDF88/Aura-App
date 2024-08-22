package viewmodel.login

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.aura.ui.home.HomeActivity
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aura.databinding.ActivityLoginBinding
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.intellij.lang.annotations.Identifier
import javax.inject.Inject


/**
 * LoginViewModel is responsible for preparing and managing the data for the {@link LoginActivity}.
 * It communicates with the API to fetch login details and provides
 * utility methods related to the notes UI.
 */
@HiltViewModel
class LoginViewModel @Inject constructor(private val repository: LoginRepository) : ViewModel() {
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

    // Handle login button click
    fun onLoginClicked() {
        viewModelScope.launch {
            _uiState.update { currentState ->
                currentState.copy(isLoading = true, isLoginButtonEnabled = false)
            }
            //appel au repository pour provoquer le login, gérer les cas d'erreur
            // si login = succes je previens l ui avec un evenement = _navigationEvents.send(NavigationEvent.NavigateToHome)
            _uiState.update { currentState ->
                currentState.copy(isLoading = false, isLoginButtonEnabled = true)
            }

        }
    }


}




