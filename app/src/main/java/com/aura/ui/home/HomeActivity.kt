package com.aura.ui.home

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.aura.R
import com.aura.databinding.ActivityHomeBinding
import com.aura.ui.login.LoginActivity
import com.aura.ui.transfer.TransferActivity
import com.aura.viewmodel.home.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import androidx.lifecycle.observe as observeLifecycle
import androidx.lifecycle.observe

/**
 * The home activity for the app.
 */
@AndroidEntryPoint
class HomeActivity : AppCompatActivity() {

    /**
     * The binding for the home layout.
     */
    private lateinit var binding: ActivityHomeBinding

    /**
     * The view model for this activity.
     */
    private val viewModel: HomeViewModel by viewModels()

    /**
     * A callback for the result of starting the TransferActivity.
     */
    private val startTransferActivityForResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            //TODO
        }

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val balance = binding.balance
        val transfer = binding.transfer
        val loading = binding.loading
        val retry = binding.retry

        /**
         * Call the view model with getUserAccounts to catch the user's accounts.
         */
        viewModel.getUserAccounts()

        /**
         * Observe accounts with the view model to display the balance and the retry button
         * if there is an error.
         */
        viewModel.accounts.observe(this) { accounts ->
            val mainAccount = accounts.find { it.main }
            if (mainAccount != null) {
                val balanceMain = mainAccount.balance
                balance.text = balanceMain.toString() + "€"
            } else {
                balance.text = getString(R.string.no_main_account)
                retry.visibility = View.VISIBLE
            }
        }
        /**
         * Retry Button click.
         */
        retry.setOnClickListener {
            viewModel.getUserAccounts()
            retry.visibility = View.GONE
        }
        /**
         * Transfer button click.
         */
        transfer.setOnClickListener {
            startTransferActivityForResult.launch(
                Intent(
                    this@HomeActivity,
                    TransferActivity::class.java
                )
            )
        }

        lifecycleScope.launch {
            viewModel.uiState.collect { uiState ->
                loading.visibility = if (uiState.isLoading) View.VISIBLE else View.GONE
                retry.visibility = if (uiState.retryButton) View.VISIBLE else View.GONE
                //error condition
                if (uiState.error.isNotBlank()) {
                    //Display Alert with error message.
                    AlertDialog.Builder(this@HomeActivity)
                        .setTitle("Erreur de connexion")
                        .setMessage(uiState.error)
                        .setPositiveButton("OK", null)
                        .show()
                    viewModel.updateErrorState("")


                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.home_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.disconnect -> {
                startActivity(Intent(this@HomeActivity, LoginActivity::class.java))
                finish()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

}



