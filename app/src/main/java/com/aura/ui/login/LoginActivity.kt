package com.aura.ui.login

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.lifecycleScope
import com.aura.databinding.ActivityLoginBinding
import com.aura.ui.home.HomeActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import viewmodel.login.LoginViewModel
import viewmodel.login.NavigationEvent

/**
 * The login activity for the app.
 */
@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    /**
     * The binding for the login layout.
     */
    private lateinit var binding: ActivityLoginBinding
    private val viewModel: LoginViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val login = binding.login
        val loading = binding.loading
        val identifier = binding.identifier
        val password = binding.password
        //ajouter binding message erreur

        identifier.addTextChangedListener {
            viewModel.onIdentifierChanged(it.toString())
        }
        password.addTextChangedListener {
            viewModel.onPasswordChanged(it.toString())
        }
        login.setOnClickListener {
            viewModel.onLoginClicked()
        }

        // Observe the UI state from the ViewModel
        lifecycleScope.launch {
            viewModel.uiState.collect { uiState ->
                login.isEnabled = uiState.isLoginButtonEnabled
                loading.visibility = if (uiState.isLoading) View.VISIBLE else View.GONE
                //mettre gestion erreur
            }
        }

        // Observe navigation events from the ViewModel
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

        /*on ne veux pas que l ui reagisse toute seul sans viewmodel
         login.setOnClickListener {
            loading.visibility = View.VISIBLE

            val intent = Intent(this@LoginActivity, HomeActivity::class.java)
            startActivity(intent)

            finish()
          }

         */

    }

}
