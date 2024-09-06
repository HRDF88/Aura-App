package com.aura.ui.transfer

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.lifecycleScope
import com.aura.databinding.ActivityTransferBinding
import com.aura.viewmodel.transfer.TransferViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

/**
 * The transfer activity for the app.
 */
@AndroidEntryPoint
class TransferActivity : AppCompatActivity() {

    /**
     * The binding for the transfer layout.
     */
    private lateinit var binding: ActivityTransferBinding

    /**
     * The view model for this activity.
     */
    private val viewModel: TransferViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityTransferBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val recipient = binding.recipient
        val amount = binding.amount
        val transfer = binding.transfer
        val loading = binding.loading

        lifecycleScope.launch {
            viewModel.uiState.collect { uiState ->
                transfer.isEnabled = uiState.isTransferButtonEnabled
                /**
                 * the UI state based on user input and linked with element xml (recipient).
                 */
                recipient.addTextChangedListener {
                    viewModel.onRecipientChanged(it.toString())
                }

                /**
                 * the UI state based on user input and linked with element xml (amount).
                 */
                amount.addTextChangedListener { text ->
                    viewModel.onAmountChanged(text.toString().toDoubleOrNull() ?: 0.0)
                }

                /**
                 * Transfer button click,
                 * error and activity management.
                 */

                transfer.setOnClickListener {
                    loading.visibility = View.VISIBLE
                    val recipientValue = recipient.text.toString()
                    val amountText = amount.text.toString()
                    val amountValue = amountText.toDoubleOrNull() ?: 0.0
                    viewModel.onTransferClicked(recipientValue, amountValue)
                    lifecycleScope.launch {
                        viewModel.uiState.collect { uiState ->
                            if (uiState.error.isNotBlank()) {
                                Toast.makeText(
                                    this@TransferActivity,
                                    uiState.error,
                                    Toast.LENGTH_LONG
                                )
                                    .show()
                                viewModel.updateErrorState("")
                            } else {
                                setResult(RESULT_OK)
                                finish()
                            }
                        }
                    }

                }
            }
        }
    }
}
