package io.coin.gari.di

import io.coin.gari.data.GariWalletRepository
import io.coin.gari.data.mapper.ApiGariWalletMapper
import io.coin.gari.data.mapper.Mapper
import io.coin.gari.domain.entity.GariWallet
import io.coin.gari.network.entity.ApiGariWallet

internal object DataModuleInjection {

    val gariWalletRepository: GariWalletRepository by lazy {
        GariWalletRepository(
            gariNetworkService = NetworkModuleInjection.gariNetworkService,
            apiGariWalletMapper = provideApiGariWalletMapper()
        )
    }

    private fun provideApiGariWalletMapper(): Mapper<ApiGariWallet, GariWallet> {
        return ApiGariWalletMapper()
    }
}