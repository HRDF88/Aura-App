package com.aura.ui.login

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.lifecycleScope
import com.aura.databinding.ActivityLoginBinding
import com.aura.ui.home.HomeActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import com.aura.viewmodel.login.LoginViewModel
import com.aura.viewmodel.login.NavigationEvent

/**
 * The login activity for the app.
 */
@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    /**
     * The binding for the login layout.
     */
    private lateinit var binding: ActivityLoginBinding

    /**
     * The view model for this activity.
     */
    private val viewModel: LoginViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val login = binding.login
        val loading = binding.loading
        val identifier = binding.identifier
        val password = binding.password


        /**
         * the UI state based on user input and linked with element xml (identifier).
         */
        identifier.addTextChangedListener {
            viewModel.onIdentifierChanged(it.toString())
        }

        /**
         * the UI state based on user input and linked with element xml (password).
         */
        password.addTextChangedListener {
            viewModel.onPasswordChanged(it.toString())
        }

        /**
         * Click login button.
         */
        login.setOnClickListener {
            val identifierValue = identifier.text.toString()
            val passwordValue = password.text.toString()
            viewModel.onLoginClicked(identifierValue, passwordValue)
        }

        /**
         * Observe the UI state from the ViewModel.
         */
        lifecycleScope.launch {
            viewModel.uiState.collect { uiState ->
                login.isEnabled = uiState.isLoginButtonEnabled
                loading.visibility = if (uiState.isLoading) View.VISIBLE else View.GONE
                //condition erreur
                if (uiState.error.isNotBlank()) {
                    Toast.makeText(this@LoginActivity, uiState.error, Toast.LENGTH_LONG).show()
                    viewModel.updateErrorState("")
                }
            }
        }

        /**
         * Observe navigation events from the ViewModel.
         */
        lifecycleScope.launch {
            viewModel.navigationEvents.collect { event ->
                when (event) {
                    is NavigationEvent.NavigateToHome -> {
                        val intent = Intent(this@LoginActivity, HomeActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                }
            }
        }
    }
}
