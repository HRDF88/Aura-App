package viewmodel.login

data class LoginUiState(
    val identifier: String = "",
    val password: String = "",
    val isLoginButtonEnabled: Boolean = false,
    val isLoading: Boolean = false,
    val error: String = "",


    )
