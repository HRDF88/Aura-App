package com.aura

import android.content.Context
import com.aura.repository.Repository
import com.aura.viewmodel.login.LoginViewModel
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.jupiter.api.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner

/**
 * LoginViewModel unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(MockitoJUnitRunner::class)
class LoginViewModelUnitTest {

    @Mock
    private lateinit var loginViewModel: LoginViewModel

    @Mock
    private lateinit var context: Context

    @Mock
    private lateinit var repository: Repository

    /**
     * Create an instance of LoginViewModelClass class using multiple dependencies such as Repository and Context.
     */
    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        loginViewModel = LoginViewModel(
            context,
            repository
        )
    }

    @Test
    fun onIdentifierChangedUpdateTest() = runBlocking {

        val identifier = "1234"

        loginViewModel.onIdentifierChanged(identifier)

        val uiState = loginViewModel.uiState.value
        assertEquals(identifier, uiState.identifier)
        assertEquals(
            identifier.isNotEmpty() && uiState.password.isNotEmpty(),
            uiState.isLoginButtonEnabled
        )

    }
}
