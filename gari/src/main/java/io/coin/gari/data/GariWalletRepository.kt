package io.coin.gari.data

import io.coin.gari.data.mapper.Mapper
import io.coin.gari.domain.entity.GariWallet
import io.coin.gari.network.entity.ApiGariWallet
import io.coin.gari.network.service.GariNetworkService

internal class GariWalletRepository(
    private val gariNetworkService: GariNetworkService,
    private val apiGariWalletMapper: Mapper<ApiGariWallet, GariWallet>
) {

    fun getWalletDetails(
        gariClientId: String,
        token: String
    ): Result<GariWallet> {
        return try {
            gariNetworkService.getWalletDetails(
                gariClientId = gariClientId,
                token = token
            ).map { apiGariWalletMapper.from(it) }
        } catch (error: Throwable) {
            Result.failure(error)
        }
    }

    fun createWallet(
        gariClientId: String,
        token: String,
        pubKey: String
    ): Result<GariWallet> {
        return try {
            gariNetworkService.createWallet(
                gariClientId = gariClientId,
                token = token,
                pubKey = pubKey
            ).map { apiGariWalletMapper.from(it) }
        } catch (error: Throwable) {
            Result.failure(error)
        }
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
    ): Result<String> {
        return gariNetworkService.sendSignedAirdropTransaction(
            gariClientId = gariClientId,
            token = token,
            pubKey = pubKey,
            airdropAmount = airdropAmount,
            encodedTransaction = encodedTransaction
        )
    }

    fun getEncodedTransaction(
        gariClientId: String,
        token: String,
        receiverPublicKey: String,
        transactionAmount: String,
    ): Result<String> {
        return gariNetworkService.getEncodedTransaction(
            gariClientId = gariClientId,
            token = token,
            receiverPublicKey = receiverPublicKey,
            transactionAmount = transactionAmount,
        )
    }
}