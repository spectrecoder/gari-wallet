package io.coin.gari.domain.web3

import android.content.Context
import android.content.Intent
import io.coin.gari.exceptions.IllegalWeb3AuthConfigException
import java8.util.concurrent.CompletableFuture

internal interface Web3AuthManager {

    @Throws(IllegalWeb3AuthConfigException::class)
    fun verifyConfig()

    fun onCreate(context: Context, intent: Intent?)

    fun onNewIntent(intent: Intent?)

    fun login(jwtToken: String): CompletableFuture<ByteArray>
}