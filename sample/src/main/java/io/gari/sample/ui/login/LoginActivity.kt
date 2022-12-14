package io.gari.sample.ui.login

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import io.gari.sample.R
import io.gari.sample.databinding.ActivityLoginBinding
import io.gari.sample.ui.wallet.details.WalletDetailsActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

class LoginActivity : AppCompatActivity() {

    private val viewModel: LoginViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = DataBindingUtil.setContentView<ActivityLoginBinding>(
            this, R.layout.activity_login
        )
        binding.clickListener = PageClickListener()
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        viewModel.action.observe(this) { it?.let { onLoginAction(it) } }
    }

    private fun onLoginAction(action: LoginAction) {
        when (action) {
            is LoginAction.Web3AuthTokenReady -> {
                openWalletDetails(action.token)
            }
        }
    }

    private fun openWalletDetails(token: String) {
        startActivity(WalletDetailsActivity.buildIntent(this, token))
        finish()
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