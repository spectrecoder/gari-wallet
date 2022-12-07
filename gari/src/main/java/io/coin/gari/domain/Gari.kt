package io.coin.gari.domain

import io.coin.gari.di.DataModuleInjection
import io.coin.gari.di.NetworkModuleInjection
import io.coin.gari.domain.entity.GariWallet
import io.coin.gari.network.core.NetworkClient

object Gari {

    private var clientId: String = ""

    private val networkClient: NetworkClient = NetworkModuleInjection.providerNetworkClient()
    private val gariNetworkService = NetworkModuleInjection.provideGariNetworkService(networkClient)
    private val gariWalletRepository =
        DataModuleInjection.provideGariWalletRepository(gariNetworkService)

    fun initialize(clientId: String) {
        this.clientId = clientId
    }

    fun setLogsEnabled(enable: Boolean) {
        networkClient.setLogsEnabled(enable)
    }

    fun getWalletDetails(token: String): Result<GariWallet> {
        return gariWalletRepository.getWalletDetails(token)
    }
}