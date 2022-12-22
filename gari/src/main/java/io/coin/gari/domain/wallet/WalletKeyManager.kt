package io.coin.gari.domain.wallet

import androidx.activity.result.ActivityResultCaller
import androidx.activity.result.contract.ActivityResultContract
import io.coin.gari.domain.entity.WalletKeyResult
import io.coin.gari.exceptions.Web3AuthorizeException
import io.coin.gari.ui.auth.web3.Web3LoginResultContract
import io.coin.gari.ui.auth.webgari.WebGariAuthResultContract
import java8.util.concurrent.CompletableFuture

class WalletKeyManager internal constructor(
    resultCaller: ActivityResultCaller
) {

    private var keyFutureResult: CompletableFuture<ByteArray>? = null

    private val keyProvider: KeyProvider = KeyProvider.WEB3AUTH
    private val web3AuthLauncher = resultCaller.registerForActivityResult(
        getResultContract()
    ) { result ->
        handleResult(result)
    }

    fun getPrivateKey(token: String): CompletableFuture<ByteArray> {
        web3AuthLauncher.launch(token)

        return CompletableFuture<ByteArray>()
            .apply { keyFutureResult = this }
    }

    private fun handleResult(result: WalletKeyResult) {
        when (result) {
            is WalletKeyResult.Success -> {
                keyFutureResult?.complete(result.key)
            }

            is WalletKeyResult.Failure -> {
                keyFutureResult?.completeExceptionally(Web3AuthorizeException())
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