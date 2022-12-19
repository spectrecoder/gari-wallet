package io.coin.gari.domain

import androidx.activity.result.ActivityResultCaller
import io.coin.gari.di.NetworkModuleInjection
import io.coin.gari.di.UseCaseModuleInjection
import io.coin.gari.domain.entity.GariWallet
import io.coin.gari.domain.entity.GariWalletState
import io.coin.gari.domain.wallet.WalletKeyManager
import java8.util.concurrent.CompletableFuture

object Gari {

    private var clientId: String = ""

    private val getWalletDetailsUseCase = UseCaseModuleInjection.getWalletDetailsUseCase
    private val createWalletUseCase = UseCaseModuleInjection.createWalletUseCase
    private val requestAirdropUseCase = UseCaseModuleInjection.requestAirdropUseCase

    fun initialize(clientId: String) {
        this.clientId = clientId
    }

    fun provideWalletKeyManager(resultCaller: ActivityResultCaller): WalletKeyManager {
        return WalletKeyManager(resultCaller)
    }

    fun setLogsEnabled(enable: Boolean) {
        NetworkModuleInjection.setLogsEnabled(enable)
    }

    fun getWalletState(token: String): GariWalletState {
        return getWalletDetailsUseCase.getWalletState(
            gariClientId = clientId,
            token = token
        )
    }

    fun createWallet(
        keyManager: WalletKeyManager,
        token: String
    ): CompletableFuture<Result<GariWallet>> {
        return keyManager.getPrivateKey(token)
            .thenApplyAsync { key ->
                createWalletUseCase.createWallet(
                    gariClientId = clientId,
                    token = token,
                    privateKey = key
                )
            }
    }

    fun getAirDrop(
        keyManager: WalletKeyManager,
        token: String,
        amount: String
    ): CompletableFuture<Result<Unit>> {
        return keyManager.getPrivateKey(token)
            .thenApplyAsync { key ->
                requestAirdropUseCase.requestAirdrop(
                    gariClientId = clientId,
                    token = token,
                    airdropAmount = amount,
                    privateKey = key
                )
            }
    }
}