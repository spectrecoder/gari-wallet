package io.coin.gari.domain.wallet

import androidx.activity.result.ActivityResultCaller
import androidx.activity.result.contract.ActivityResultContract
import io.coin.gari.domain.entity.WalletKeyResult
import io.coin.gari.ui.auth.web3.Web3LoginResultContract
import io.coin.gari.ui.auth.webgari.WebGariAuthResultContract

class WalletKeyManager internal constructor(
    resultCaller: ActivityResultCaller
) {

    private var onSuccess: ((ByteArray) -> Unit)? = null
    private var onFailure: (() -> Unit)? = null

    private val keyProvider: KeyProvider = KeyProvider.WEB3AUTH
    private val web3AuthLauncher = resultCaller.registerForActivityResult(
        getResultContract()
    ) { result ->
        handleResult(result)
    }

    fun getPrivateKey(
        token: String,
        onSuccess: (ByteArray) -> Unit,
        onFailure: () -> Unit
    ) {
        this.onSuccess = onSuccess
        this.onFailure = onFailure

        web3AuthLauncher.launch(token)
    }

    private fun handleResult(result: WalletKeyResult) {
        when (result) {
            is WalletKeyResult.Success -> {
                onSuccess?.invoke(result.key)
            }

            is WalletKeyResult.Failure -> {
                onFailure?.invoke()
            }

            is WalletKeyResult.Canceled -> {
                /* no op */
            }
        }
    }

    private fun getResultContract(): ActivityResultContract<String, WalletKeyResult> {
        return when (keyProvider) {
            KeyProvider.WEB3AUTH -> Web3LoginResultContract()
            KeyProvider.GARI -> WebGariAuthResultContract()
        }
    }

    private enum class KeyProvider {
        WEB3AUTH,
        GARI
    }
}