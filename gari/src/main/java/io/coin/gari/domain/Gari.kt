package io.coin.gari.domain

import io.coin.gari.di.DataModuleInjection
import io.coin.gari.di.NetworkModuleInjection
import io.coin.gari.di.UseCaseModuleInjection
import io.coin.gari.domain.entity.GariWallet
import io.coin.gari.domain.entity.GariWalletState
import io.coin.gari.network.core.NetworkClient

object Gari {

    private var clientId: String = ""

    private val getWalletDetailsUseCase = UseCaseModuleInjection.getWalletDetailsUseCase
    private val requestAirdropUseCase = UseCaseModuleInjection.requestAirdropUseCase

    private val networkClient: NetworkClient = NetworkModuleInjection
        .providerNetworkClient()

    private val gariWalletRepository = DataModuleInjection.gariWalletRepository

    fun initialize(clientId: String) {
        this.clientId = clientId
    }

    fun setLogsEnabled(enable: Boolean) {
        networkClient.setLogsEnabled(enable)
    }

    fun getWalletState(token: String): GariWalletState {
        return getWalletDetailsUseCase.getWalletState(
            gariClientId = clientId,
            token = token
        )
    }

    fun createWallet(token: String, pubKey: String): Result<GariWallet> {
        return gariWalletRepository.createWallet(
            gariClientId = clientId,
            token = token,
            pubKey = pubKey
        )
    }

    fun getAirDrop(
        token: String,
        pubKey: String,
        privateKey: ByteArray,
        amount: String
    ): Result<Unit> {
        return requestAirdropUseCase.requestAirdrop(
            gariClientId = clientId,
            token = token,
            pubKey = pubKey,
            airdropAmount = amount,
            privateKey = privateKey
        )
    }
}