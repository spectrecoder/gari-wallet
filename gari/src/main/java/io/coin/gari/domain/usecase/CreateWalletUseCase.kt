package io.coin.gari.domain.usecase

import io.coin.gari.data.GariWalletRepository
import io.coin.gari.domain.entity.GariWallet

internal class CreateWalletUseCase(
    private val gariWalletRepository: GariWalletRepository
) {

    fun createWallet(
        gariClientId: String,
        token: String,
        publicKey: String
    ): Result<GariWallet> {
        return gariWalletRepository.createWallet(
            gariClientId = gariClientId,
            token = token,
            pubKey = publicKey
        )
    }
}