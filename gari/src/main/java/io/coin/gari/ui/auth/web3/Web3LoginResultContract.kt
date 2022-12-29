package io.coin.gari.ui.auth.web3

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.activity.result.contract.ActivityResultContract
import io.coin.gari.domain.entity.WalletKeyResult
import io.coin.gari.ui.auth.core.AuthResult

internal class Web3LoginResultContract : ActivityResultContract<String, WalletKeyResult>() {

    override fun createIntent(context: Context, input: String): Intent {
        return Web3LoginActivity.buildIntent(
            context = context,
            token = input
        )
    }

    override fun parseResult(
        resultCode: Int,
        intent: Intent?
    ): WalletKeyResult {
        return when (resultCode) {
            Activity.RESULT_OK -> {
                intent
                    ?.extras
                    ?.getParcelable<WalletKeyResult>(AuthResult.WALLET_KEY_RESULT)
                    ?: return WalletKeyResult.Failure
            }

            else -> {
                WalletKeyResult.Canceled
            }
        }
    }
}