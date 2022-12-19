package io.coin.gari.domain.usecase

import io.coin.gari.data.GariWalletRepository
import io.coin.gari.domain.entity.GariWalletState
import io.coin.gari.exceptions.WalletNotRegisteredException

internal class GetWalletDetailsUseCase(
    private val gariWalletRepository: GariWalletRepository
) {

    fun getWalletState(
        gariClientId: String,
        token: String
    ): GariWalletState {
        val walletDetailsResult = gariWalletRepository.getWalletDetails(
            gariClientId = gariClientId,
            token = token
        )

        val gariWallet = walletDetailsResult.getOrNull()

        if (walletDetailsResult.isFailure || gariWallet == null) {
            val error = walletDetailsResult.exceptionOrNull()

            return if (error is WalletNotRegisteredException) {
                GariWalletState.NotExist
            } else {
                GariWalletState.Error(error)
            }
        }

        return GariWalletState.Activated(
            pubKey = gariWallet.publicKey,
            balance = gariWallet.balance
        )
    }
}