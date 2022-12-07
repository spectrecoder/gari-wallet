package io.coin.gari.di

import io.coin.gari.data.GariWalletRepository
import io.coin.gari.network.core.NetworkClient
import io.coin.gari.network.service.GariNetworkService

internal object DataModuleInjection {

    fun provideGariWalletRepository(gariNetworkService: GariNetworkService): GariWalletRepository {
        return GariWalletRepository(gariNetworkService = gariNetworkService)
    }
}