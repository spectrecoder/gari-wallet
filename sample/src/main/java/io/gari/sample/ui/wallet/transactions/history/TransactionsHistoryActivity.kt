package io.gari.sample.ui.wallet.transactions.history

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import io.gari.sample.R
import io.gari.sample.databinding.ActivityTransactionsHistoryBinding
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class TransactionsHistoryActivity : AppCompatActivity() {

    private lateinit var screenBinding: ActivityTransactionsHistoryBinding

    private val web3AuthToken: String
        get() = intent.getStringExtra(ARG_TOKEN)
            ?: throw IllegalStateException("Forget to pass web3 auth token?")

    private val viewModel: TransactionsHistoryViewModel by viewModel { parametersOf(web3AuthToken) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        screenBinding = DataBindingUtil.setContentView<ActivityTransactionsHistoryBinding>(
            this, R.layout.activity_transactions_history
        )
        screenBinding.clickListener = PageClickListener()
        screenBinding.lifecycleOwner = this
        screenBinding.viewModel = viewModel

        screenBinding.toolbarTransactions.setNavigationOnClickListener {
            finish()
        }
    }

    private class PageClickListener : View.OnClickListener {

        override fun onClick(view: View) {
        }
    }

    private companion object {

        private const val ARG_TOKEN = "ARG_TOKEN"

        fun buildIntent(context: Context, token: String): Intent {
            return Intent(context, TransactionsHistoryActivity::class.java)
                .also { it.putExtra(ARG_TOKEN, token) }
        }
    }
}