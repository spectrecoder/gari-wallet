package io.gari.sample.ui.wallet.details

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import io.coin.gari.domain.Gari
import io.coin.gari.domain.entity.GariWalletState
import io.gari.sample.R
import io.gari.sample.databinding.ActivityWalletDetailsBinding
import io.gari.sample.ui.login.LoginActivity
import io.gari.sample.ui.wallet.airdrop.AirdropActivity
import io.gari.sample.ui.wallet.transactions.send.SendTransactionActivity
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class WalletDetailsActivity : AppCompatActivity() {

    private val web3AuthToken: String
        get() = intent.getStringExtra(ARG_TOKEN)
            ?: throw IllegalStateException("Forget to pass web3 auth token?")

    private lateinit var screenBinding: ActivityWalletDetailsBinding
    private val walletKeyManager = Gari.provideWalletKeyManager(this)

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
        screenBinding.tvWalletStatusTitle.isVisible = true
        screenBinding.containerWalletNotActivated.isVisible = false
        screenBinding.containerWalletActivated.isVisible = true
        screenBinding.containerCheckFailure.isVisible = false
        screenBinding.tvBalance.text = state.balance
        screenBinding.tvPubKey.text = state.pubKey
    }

    private fun renderStateNotExist() {
        screenBinding.tvWalletStatusTitle.isVisible = true
        screenBinding.containerWalletNotActivated.isVisible = true
        screenBinding.containerWalletActivated.isVisible = false
        screenBinding.containerCheckFailure.isVisible = false
    }

    private fun renderStateError(state: GariWalletState.Error) {
        screenBinding.tvWalletStatusTitle.isVisible = true
        screenBinding.containerWalletNotActivated.isVisible = false
        screenBinding.containerWalletActivated.isVisible = false
        screenBinding.containerCheckFailure.isVisible = true

        screenBinding.tvErrorDescription.text = state.error?.stackTraceToString()
    }

    private fun activateWallet() {
        viewModel.registerWallet(walletKeyManager)
    }

    private fun reloadBalance() {
        viewModel.reloadBalance()
    }

    private fun logout() {
        startActivity(Intent(this, LoginActivity::class.java))
        finishAffinity()
    }

    private fun requestAirdrop() {
        val walletState = viewModel.walletState.value

        if (walletState !is GariWalletState.Activated) {
            // todo: wallet has not been activated, requires activating first
            return
        }

        startActivity(
            AirdropActivity.buildIntent(
                context = this,
                token = web3AuthToken,
                publicKey = walletState.pubKey
            )
        )
    }

    private fun sendTransaction() {
        startActivity(
            SendTransactionActivity.buildIntent(
                context = this,
                token = web3AuthToken
            )
        )
    }

    private inner class PageClickListener : View.OnClickListener {

        override fun onClick(view: View) {
            when (view.id) {
                R.id.btnActivateWallet -> activateWallet()
                R.id.btnRequestAirdrop -> requestAirdrop()
                R.id.btnSendTransaction -> sendTransaction()
                R.id.btnReloadBalance -> reloadBalance()
                R.id.btnLogout -> logout()
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