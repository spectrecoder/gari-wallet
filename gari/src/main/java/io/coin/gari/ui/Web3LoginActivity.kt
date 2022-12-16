package io.coin.gari.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import io.coin.gari.domain.web3auth.Web3AuthManager
import io.coin.gari.domain.web3auth.Web3AuthManagerImpl

class Web3LoginActivity : AppCompatActivity() {

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
                    cancelLoginRequest()
                } else {
                    completeLogin(privateKey)
                }
            }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)

        web3AuthManager.onNewIntent(intent)
    }

    private fun cancelLoginRequest() {
        setResult(RESULT_CANCELED)
        finish()
    }

    private fun completeLogin(privateKey: ByteArray) {
        val resultIntent = Intent()
        resultIntent.putExtra(WALLET_KEY, privateKey)
        setResult(RESULT_OK, resultIntent)
        finish()
    }

    companion object {

        private const val ARG_USER_TOKEN = "ARG_USER_TOKEN"
        const val WALLET_KEY = "WALLET_KEY"

        fun buildIntent(context: Context, token: String): Intent {
            return Intent(context, Web3LoginActivity::class.java).also {
                it.putExtra(
                    ARG_USER_TOKEN,
                    token
                )
            }
        }
    }
}