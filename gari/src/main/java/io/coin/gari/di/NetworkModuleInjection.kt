package io.coin.gari.di

import io.coin.gari.network.core.NetworkClient
import io.coin.gari.network.service.GariNetworkService

internal object NetworkModuleInjection {

    val gariNetworkService: GariNetworkService by lazy {
        GariNetworkService(
            networkClient = providerNetworkClient()
        )
    }

    fun providerNetworkClient(): NetworkClient {
        return NetworkClient.Builder()
            .build()
    }

    fun provideGariNetworkService(networkClient: NetworkClient): GariNetworkService {
        return GariNetworkService(
            networkClient = networkClient
        )
    }
}