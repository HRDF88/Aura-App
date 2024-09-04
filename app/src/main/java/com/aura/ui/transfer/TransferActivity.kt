package com.aura.ui.transfer

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.lifecycleScope
import com.aura.databinding.ActivityTransferBinding
import com.aura.viewmodel.transfer.TransferViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import javax.inject.Scope

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

        recipient.addTextChangedListener {
            viewModel.onRecipientChanged(it.toString())
        }
        amount.addTextChangedListener { text ->
            viewModel.onAmountChanged(text.toString().toDoubleOrNull() ?: 0.0)
        }
        lifecycleScope.launch {
            viewModel.uiState.collect { uiState ->
                transfer.isEnabled = uiState.isTransferButtonEnabled
            }
        }
        transfer.setOnClickListener {
            loading.visibility = View.VISIBLE
            val recipientValue = recipient.text.toString()
            val amountText = amount.text.toString()
            val amountValue = amountText.toDoubleOrNull() ?: 0.0
            viewModel.onTransferClicked(recipientValue, amountValue)

            setResult(RESULT_OK)
            finish()
        }
    }
}
