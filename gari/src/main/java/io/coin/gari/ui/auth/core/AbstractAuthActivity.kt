package io.coin.gari.ui.auth.core

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import io.coin.gari.domain.entity.WalletKeyResult

internal abstract class AbstractAuthActivity : AppCompatActivity() {

    protected fun cancelLoginRequest() {
        setResult(RESULT_CANCELED)
        finish()
    }

    protected fun completeLoginSuccess(privateKey: ByteArray) {
        val resultIntent = Intent()
        resultIntent.putExtra(AuthResult.WALLET_KEY_RESULT, WalletKeyResult.Success(privateKey))
        setResult(RESULT_OK, resultIntent)
        finish()
    }

    protected fun completeLoginFailure(error: Throwable) {
        val resultIntent = Intent()
        resultIntent.putExtra(AuthResult.WALLET_KEY_RESULT, WalletKeyResult.Failure)
        setResult(RESULT_OK, resultIntent)
        finish()
    }
}