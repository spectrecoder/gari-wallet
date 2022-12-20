package io.coin.gari.domain.wallet

import androidx.activity.result.ActivityResultCaller
import io.coin.gari.exceptions.Web3AuthorizeException
import io.coin.gari.ui.web3.WalletKeyResult
import io.coin.gari.ui.web3.Web3LoginResultContract
import java8.util.concurrent.CompletableFuture

class WalletKeyManager internal constructor(
    resultCaller: ActivityResultCaller
) {

    private var keyFutureResult: CompletableFuture<ByteArray>? = null

    private val web3AuthLauncher = resultCaller.registerForActivityResult(
        Web3LoginResultContract()
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
}