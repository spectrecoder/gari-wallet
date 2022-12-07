package io.gari.sample.ui.wallet.details

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import io.gari.sample.R
import io.gari.sample.databinding.ActivityWalletDetailsBinding
import io.gari.sample.domain.web3auth.Web3AuthManagerImpl
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class WalletDetailsActivity : AppCompatActivity() {

    private val web3AuthToken: String
        get() = intent.getStringExtra(ARG_TOKEN)
            ?: throw IllegalStateException("Forget to pass web3 auth token?")

    private val viewModel: WalletDetailsViewModel by viewModel { parametersOf(web3AuthToken) }
    private val web3AuthManager: Web3AuthManagerImpl by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = DataBindingUtil.setContentView<ActivityWalletDetailsBinding>(
            this, R.layout.activity_wallet_details
        )
        binding.clickListener = PageClickListener()
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        web3AuthManager.onCreate(this, intent)
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)

        web3AuthManager.onNewIntent(intent)
    }

    private inner class PageClickListener : View.OnClickListener {

        override fun onClick(view: View) {
            when (view.id) {
            }
        }
    }

    companion object {

        private const val ARG_TOKEN = "ARG_TOKEN"

        fun buildIntent(context: Context, token: String): Intent {
            return Intent(context, WalletDetailsActivity::class.java)
                .also { it.putExtra(ARG_TOKEN, token) }
        }
    }
}