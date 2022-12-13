package io.coin.gari.data

import io.coin.gari.domain.entity.GariWallet
import io.coin.gari.network.service.GariNetworkService

internal class GariWalletRepository(
    private val gariNetworkService: GariNetworkService
) {

    fun getWalletDetails(
        gariClientId: String, token: String
    ): Result<GariWallet> {
        return gariNetworkService.getWalletDetails(
            gariClientId = gariClientId, token = token
        ).map { GariWallet() }
    }

    fun createWallet(
        gariClientId: String, token: String, pubKey: String
    ): Result<GariWallet> {
        return gariNetworkService.createWallet(
            gariClientId = gariClientId, token = token, pubKey = pubKey
        ).map { GariWallet() }
    }

    fun getEncodedAirdropTransaction(
        gariClientId: String,
        token: String,
        pubKey: String,
        airdropAmount: String,
    ): Result<String> {
        return gariNetworkService.getEncodedAirdropTransaction(
            gariClientId = gariClientId,
            token = token,
            pubKey = pubKey,
            airdropAmount = airdropAmount
        )
    }

    fun sendSignedAirdropTransaction(
        gariClientId: String,
        token: String,
        pubKey: String,
        airdropAmount: String,
        encodedTransaction: String,
    ): Result<Unit> {
        return gariNetworkService.sendSignedAirdropTransaction(
            gariClientId = gariClientId,
            token = token,
            pubKey = pubKey,
            airdropAmount = airdropAmount,
            encodedTransaction = encodedTransaction
        )
    }
}