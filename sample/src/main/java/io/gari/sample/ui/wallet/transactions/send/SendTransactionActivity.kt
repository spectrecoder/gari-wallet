package io.gari.sample.ui.wallet.transactions.send

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import io.coin.gari.domain.Gari
import io.gari.sample.R
import io.gari.sample.databinding.ActivitySendTransactionBinding
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class SendTransactionActivity : AppCompatActivity() {

    private val web3AuthToken: String
        get() = intent.getStringExtra(ARG_TOKEN)
            ?: throw IllegalStateException("Forget to pass web3 auth token?")

    private lateinit var screenBinding: ActivitySendTransactionBinding
    private val walletKeyManager = Gari.provideWalletKeyManager(this)

    private val viewModel: SendTransactionViewModel by viewModel { parametersOf(web3AuthToken) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        screenBinding = DataBindingUtil.setContentView<ActivitySendTransactionBinding>(
            this, R.layout.activity_send_transaction
        )
        screenBinding.clickListener = PageClickListener()
        screenBinding.lifecycleOwner = this
        screenBinding.viewModel = viewModel

        screenBinding.toolbarSendTransaction.setNavigationOnClickListener {
            finish()
        }

        viewModel.viewState.observe(this) { it?.let { renderViewState(it) } }
    }

    private fun renderViewState(state: TransactionViewState) {
        when (state) {
            is TransactionViewState.Ready -> renderTransactionForm()
            is TransactionViewState.Completed -> renderAirdropResult(state.signature)
        }
    }

    private fun renderTransactionForm() {
        screenBinding.containerTransactionSignature.isVisible = false
        screenBinding.btnSendTransaction.isVisible = true
    }

    private fun renderAirdropResult(signature: String) {
        screenBinding.containerTransactionSignature.isVisible = true
        screenBinding.btnSendTransaction.isVisible = false
        screenBinding.tvSignature.text = signature
    }

    private fun sendTransaction() {
        viewModel.sendTransaction(walletKeyManager)
    }

    private inner class PageClickListener : View.OnClickListener {

        override fun onClick(view: View) {
            when (view.id) {
                R.id.btnSendTransaction -> sendTransaction()
            }
        }
    }

    companion object {

        private const val ARG_TOKEN = "ARG_TOKEN"

        fun buildIntent(context: Context, token: String): Intent {
            return Intent(context, SendTransactionActivity::class.java)
                .also { it.putExtra(ARG_TOKEN, token) }
        }
    }
}