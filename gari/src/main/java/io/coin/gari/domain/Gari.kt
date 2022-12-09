package io.coin.gari.domain

import android.content.Context
import android.content.Intent
import io.coin.gari.di.DataModuleInjection
import io.coin.gari.di.NetworkModuleInjection
import io.coin.gari.domain.entity.GariWallet
import io.coin.gari.domain.web3auth.Web3AuthManager
import io.coin.gari.domain.web3auth.Web3AuthManagerImpl
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

    fun getWalletDetails(
        context: Context,
        intent: Intent,
        token: String
    ) {
        val walletResult = gariWalletRepository.getWalletDetails(
            gariClientId = clientId,
            token = token
        )
    }
}