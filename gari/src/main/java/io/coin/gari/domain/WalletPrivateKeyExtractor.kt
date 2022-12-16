package io.coin.gari.domain

import android.app.Activity
import android.content.Context
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCaller
import androidx.activity.result.contract.ActivityResultContracts
import io.coin.gari.ui.Web3LoginActivity

class WalletPrivateKeyExtractor(
    resultCaller: ActivityResultCaller
) {

    private val web3AuthLauncher = resultCaller.registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        when (result.resultCode) {
            Activity.RESULT_OK -> {
                onResultIntent(result)
            }

            Activity.RESULT_CANCELED -> {
                onCanceled()
            }
        }
    }

    fun getPrivateKey(context: Context, token: String) {
        web3AuthLauncher.launch(
            Web3LoginActivity.buildIntent(
                context = context,
                token = token
            )
        )
    }

    private fun onResultIntent(result: ActivityResult) {
        val key = result.data
            ?.extras
            ?.getByteArray(Web3LoginActivity.WALLET_KEY)

        if (key == null) {
            // todo: handle failed
        } else {
            // todo: handle success
        }
    }

    private fun onCanceled() {
        // todo: handle failed
    }
}