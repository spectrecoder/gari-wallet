package io.gari.sample.ui.login

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import io.gari.sample.R
import io.gari.sample.databinding.ActivityLoginBinding
import io.gari.sample.domain.web3auth.Web3AuthManagerImpl
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class LoginActivity : AppCompatActivity() {

    private val viewModel: LoginViewModel by viewModel()
    private val web3AuthManager: Web3AuthManagerImpl by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = DataBindingUtil.setContentView<ActivityLoginBinding>(
            this, R.layout.activity_login
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
                R.id.btnLogin -> {
                    viewModel.doLogin()
                }
            }
        }
    }
}