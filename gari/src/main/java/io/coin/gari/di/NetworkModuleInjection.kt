package io.coin.gari.di

import io.coin.gari.network.core.NetworkClient
import io.coin.gari.network.service.GariNetworkService

internal object NetworkModuleInjection {

    private val networkClient: NetworkClient by lazy {
        NetworkClient.Builder()
            .build()
    }

    val gariNetworkService: GariNetworkService by lazy {
        GariNetworkService(
            networkClient = networkClient
        )
    }

    fun setLogsEnabled(enable: Boolean) {
        networkClient.setLogsEnabled(enable)
    }
}