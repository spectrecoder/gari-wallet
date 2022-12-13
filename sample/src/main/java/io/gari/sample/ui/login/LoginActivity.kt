package io.gari.sample.ui.login

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import io.coin.gari.domain.Gari
import io.coin.gari.domain.web3auth.Web3AuthManager
import io.coin.gari.domain.web3auth.Web3AuthManagerImpl
import io.gari.sample.R
import io.gari.sample.databinding.ActivityLoginBinding
import io.gari.sample.ui.wallet.details.WalletDetailsActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class LoginActivity : AppCompatActivity() {

    private val viewModel: LoginViewModel by viewModel()
    private lateinit var web3AuthManager: Web3AuthManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        web3AuthManager = Web3AuthManagerImpl()
        web3AuthManager.onCreate(this, intent)

        val binding = DataBindingUtil.setContentView<ActivityLoginBinding>(
            this, R.layout.activity_login
        )
        binding.clickListener = PageClickListener()
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        viewModel.action.observe(this) { it?.let { onLoginAction(it) } }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)

        web3AuthManager.onNewIntent(intent)
    }

    private fun onLoginAction(action: LoginAction) {
        when (action) {
            is LoginAction.Web3AuthTokenReady -> {
                openWalletDetails(action.token)
            }
        }
    }

    private fun openWalletDetails(token: String) {

        web3AuthManager.login(token)
            .whenComplete { keyPair, u ->
                lifecycleScope.launch(Dispatchers.IO) {
                    Gari.createWallet(token, keyPair.second)

                    Gari.getAirDrop(
                        token = token,
                        pubKey = keyPair.second,
                        privateKey = keyPair.first,
                        amount = "100000"
                    )
                }
            }

        /*startActivity(WalletDetailsActivity.buildIntent(this, token))
        finish()*/
    }

    private inner class PageClickListener : View.OnClickListener {

        override fun onClick(view: View) {
            when (view.id) {
                R.id.btnLogin -> {
                    viewModel.doLogin()
                }
            }
        }
    }
}