package com.aura.viewmodel.home

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aura.bo.AccountResponse
import com.aura.R
import com.aura.service.HomeApiService
import com.aura.repository.HomeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
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
    private val repository: HomeRepository
) : ViewModel() {
    // Create TAG for logging
    companion object {
        private const val TAG = "HomeViewModel"
    }

    private val _accounts = MutableLiveData<List<AccountResponse>>()
    val accounts: LiveData<List<AccountResponse>> = _accounts
    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()


    private fun onError(errorMessageHome: String) {
        _uiState.update { currentState ->
            currentState.copy(
                error = errorMessageHome,
                isLoading = false,

                )
        }
    }

    //update de errorMessage pour éviter boucle
    fun updateErrorState(errorMessageHome: String) {
        val currentState = uiState.value
        val updatedState = currentState.copy(error = errorMessageHome)
        _uiState.value = updatedState
    }

    fun getUserAccounts(userId: String) {
        viewModelScope.launch {
            _uiState.update { currentState ->
                currentState.copy(
                    isLoading = true,
                )
            }
            _uiState.update { currentState ->
                currentState.copy(
                    isLoading = false,
                )
            }
            try {
                val accounts = withContext(Dispatchers.IO) {
                    repository.getUserAccounts()
                }
                _accounts.value = accounts
                if (accounts.isEmpty()){onError(errorMessageHome = context.getString(R.string.error_get_account))
                }
            } catch (e: Exception) {
                //gérer autres erreurs
                val errorMessageHome = context.getString(R.string.unspecified_error)
                onError(errorMessageHome)
                Log.e(TAG, errorMessageHome, e)
                _uiState.update { currentState ->
                    currentState.copy(
                        isLoading = false,
                    )
                }
            } catch (e: IOException) {
                //gérer l'exception d'absence de connexion Internet'
                val errorMessageHome = context.getString(R.string.error_no_Internet)
                onError(errorMessageHome)
                Log.e(TAG, errorMessageHome, e)
                _uiState.update { currentState ->
                    currentState.copy(
                        isLoading = false,


                        )
                }
            }
        }
    }
}



