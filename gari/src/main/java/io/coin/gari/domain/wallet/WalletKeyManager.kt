package io.coin.gari.domain.wallet

import androidx.activity.result.ActivityResultCaller
import androidx.activity.result.contract.ActivityResultContract
import io.coin.gari.domain.entity.WalletKeyResult
import io.coin.gari.domain.web3.Web3AuthConfig
import io.coin.gari.ui.auth.core.AuthConfigArgs
import io.coin.gari.ui.auth.web3.Web3LoginResultContract
import io.coin.gari.ui.auth.webgari.WebGariAuthResultContract

class WalletKeyManager internal constructor(
    resultCaller: ActivityResultCaller
) {

    private var onSuccess: ((ByteArray) -> Unit)? = null
    private var onFailure: (() -> Unit)? = null
    private var onCancel: (() -> Unit)? = null

    private val keyProvider: KeyProvider = KeyProvider.WEB3AUTH
    private val web3AuthLauncher = resultCaller.registerForActivityResult(
        getResultContract()
    ) { result ->
        handleResult(result)
    }

    internal fun getPrivateKey(
        token: String,
        web3AuthConfig: Web3AuthConfig,
        onSuccess: (ByteArray) -> Unit,
        onFailure: () -> Unit,
        onCancel: () -> Unit
    ) {
        this.onSuccess = onSuccess
        this.onFailure = onFailure
        this.onCancel = onCancel

        web3AuthLauncher.launch(getAuthArgs(token, web3AuthConfig))
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
                onCancel?.invoke()
            }
        }
    }

    private fun getResultContract(): ActivityResultContract<AuthConfigArgs, WalletKeyResult> {
        return when (keyProvider) {
            KeyProvider.WEB3AUTH -> Web3LoginResultContract()
            KeyProvider.GARI -> WebGariAuthResultContract()
        }
    }

    private fun getAuthArgs(jwtToken: String, web3AuthConfig: Web3AuthConfig): AuthConfigArgs {
        return when (keyProvider) {
            KeyProvider.WEB3AUTH -> {
                AuthConfigArgs.Web3AuthArgs(
                    jwtToken = jwtToken,
                    web3AuthConfig = web3AuthConfig
                )
            }

            KeyProvider.GARI -> {
                AuthConfigArgs.WebViewArgs(
                    jwtToken = jwtToken
                )
            }
        }
    }

    private enum class KeyProvider {
        WEB3AUTH,
        GARI
    }
}