package io.coin.gari.data

import io.coin.gari.domain.entity.GariWallet
import io.coin.gari.network.service.GariNetworkService

internal class GariWalletRepository(
    private val gariNetworkService: GariNetworkService
) {

    fun getWalletDetails(token: String): Result<GariWallet> {
        return gariNetworkService.getWalletDetails(token)
            .map { GariWallet() }
    }
}