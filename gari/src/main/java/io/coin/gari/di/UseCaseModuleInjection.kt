package io.coin.gari.di

import io.coin.gari.domain.base64.AndroidBase64Util
import io.coin.gari.domain.usecase.CreateWalletUseCase
import io.coin.gari.domain.usecase.GetWalletDetailsUseCase
import io.coin.gari.domain.usecase.RequestAirdropUseCase

internal object UseCaseModuleInjection {

    val getWalletDetailsUseCase: GetWalletDetailsUseCase by lazy {
        GetWalletDetailsUseCase(
            gariWalletRepository = DataModuleInjection.gariWalletRepository
        )
    }

    val requestAirdropUseCase: RequestAirdropUseCase by lazy {
        RequestAirdropUseCase(
            gariWalletRepository = DataModuleInjection.gariWalletRepository,
            base64Util = AndroidBase64Util()
        )
    }

    val createWalletUseCase: CreateWalletUseCase by lazy {
        CreateWalletUseCase(
            gariWalletRepository = DataModuleInjection.gariWalletRepository
        )
    }
}