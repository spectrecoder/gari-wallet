package io.gari.sample.ui.wallet.details

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import io.coin.gari.domain.entity.GariWalletState
import io.gari.sample.R
import io.gari.sample.databinding.ActivityWalletDetailsBinding
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class WalletDetailsActivity : AppCompatActivity() {

    private val web3AuthToken: String
        get() = intent.getStringExtra(ARG_TOKEN)
            ?: throw IllegalStateException("Forget to pass web3 auth token?")

    private lateinit var screenBinding: ActivityWalletDetailsBinding

    private val viewModel: WalletDetailsViewModel by viewModel { parametersOf(web3AuthToken) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        screenBinding = DataBindingUtil.setContentView<ActivityWalletDetailsBinding>(
            this, R.layout.activity_wallet_details
        )
        screenBinding.clickListener = PageClickListener()
        screenBinding.lifecycleOwner = this
        screenBinding.viewModel = viewModel

        viewModel.walletState.observe(this) {
            it?.let { renderState(it) }
        }
    }

    private fun renderState(state: GariWalletState) {
        when (state) {
            is GariWalletState.Activated -> renderStateActivated(state)
            is GariWalletState.NotExist -> renderStateNotExist()
            is GariWalletState.Error -> renderStateError(state)
        }
    }

    private fun renderStateActivated(state: GariWalletState.Activated) {
        screenBinding.containerWalletNotActivated.isVisible = false
        screenBinding.containerWalletActivated.isVisible = true
        screenBinding.containerCheckFailure.isVisible = false
    }

    private fun renderStateNotExist() {
        screenBinding.containerWalletNotActivated.isVisible = true
        screenBinding.containerWalletActivated.isVisible = false
        screenBinding.containerCheckFailure.isVisible = false
    }

    private fun renderStateError(state: GariWalletState.Error) {
        screenBinding.containerWalletNotActivated.isVisible = false
        screenBinding.containerWalletActivated.isVisible = false
        screenBinding.containerCheckFailure.isVisible = true

        screenBinding.tvErrorDescription.text = state.error?.stackTraceToString()
    }

    private fun activateWallet() {

    }

    private inner class PageClickListener : View.OnClickListener {

        override fun onClick(view: View) {
            when (view.id) {
                R.id.btnActivateWallet -> activateWallet()
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