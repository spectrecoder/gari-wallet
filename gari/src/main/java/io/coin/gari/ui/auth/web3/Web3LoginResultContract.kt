package io.coin.gari.ui.auth.web3

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.activity.result.contract.ActivityResultContract
import io.coin.gari.domain.entity.WalletKeyResult
import io.coin.gari.ui.auth.core.AuthConfigArgs
import io.coin.gari.ui.auth.core.AuthResult

internal class Web3LoginResultContract : ActivityResultContract<AuthConfigArgs, WalletKeyResult>() {

    override fun createIntent(context: Context, input: AuthConfigArgs): Intent {
        val args = input as? AuthConfigArgs.Web3AuthArgs
            ?: throw IllegalStateException("Unexpected arguments")

        return Web3LoginActivity.buildIntent(
            context = context,
            token = args.jwtToken,
            config = args.web3AuthConfig
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