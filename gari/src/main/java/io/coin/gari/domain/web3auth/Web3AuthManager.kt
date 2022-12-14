package io.coin.gari.domain.web3auth

import android.content.Context
import android.content.Intent
import java8.util.concurrent.CompletableFuture

internal interface Web3AuthManager {

    fun onCreate(context: Context, intent: Intent?)

    fun onNewIntent(intent: Intent?)

    fun login(jwtToken: String): CompletableFuture<ByteArray>
}