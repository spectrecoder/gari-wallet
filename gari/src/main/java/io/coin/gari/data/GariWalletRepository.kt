package io.coin.gari.data

import io.coin.gari.domain.entity.GariWallet
import io.coin.gari.network.service.GariNetworkService

internal class GariWalletRepository(
    private val gariNetworkService: GariNetworkService
) {

    fun getWalletDetails(
        gariClientId: String,
        token: String
    ): Result<GariWallet> {
        return gariNetworkService.getWalletDetails(gariClientId, token)
            .map { GariWallet() }
    }

    fun createWallet(
        gariClientId: String,
        token: String,
        pubKey: String
    ): Result<GariWallet> {
        return gariNetworkService.createWallet(gariClientId, token, pubKey)
            .map { GariWallet() }
    }
}