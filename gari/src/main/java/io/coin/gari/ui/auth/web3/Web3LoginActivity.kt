package io.coin.gari.ui.auth.web3

import android.content.Context
import android.content.Intent
import android.os.Bundle
import io.coin.gari.domain.web3.Web3AuthManager
import io.coin.gari.domain.web3.Web3AuthManagerImpl
import io.coin.gari.exceptions.Web3AuthorizeException
import io.coin.gari.ui.auth.core.AbstractAuthActivity

internal class Web3LoginActivity : AbstractAuthActivity() {

    private lateinit var web3AuthManager: Web3AuthManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val jwtToken = intent.getStringExtra(ARG_USER_TOKEN)

        if (jwtToken.isNullOrEmpty()) {
            cancelLoginRequest()
            return
        }

        web3AuthManager = Web3AuthManagerImpl()
        web3AuthManager.onCreate(this, intent)

        web3AuthManager.login(jwtToken)
            .whenComplete { privateKey, error ->
                if (error != null || privateKey == null) {
                    completeLoginFailure(error ?: Web3AuthorizeException("Undefined web3 error"))
                } else {
                    completeLoginSuccess(privateKey)
                }
            }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)

        web3AuthManager.onNewIntent(intent)
    }

    companion object {

        private const val ARG_USER_TOKEN = "ARG_USER_TOKEN"

        fun buildIntent(
            context: Context,
            token: String
        ): Intent {
            return Intent(context, Web3LoginActivity::class.java)
                .also { it.putExtra(ARG_USER_TOKEN, token) }
        }
    }
}