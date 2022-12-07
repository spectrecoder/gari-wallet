package io.coin.gari.network.service

import io.coin.gari.network.Api
import io.coin.gari.network.core.NetworkClient
import io.coin.gari.network.entity.ApiGariWallet

internal class GariNetworkService(
    private val networkClient: NetworkClient
) {

    fun getWalletDetails(
        gariClientId: String,
        token: String
    ): Result<ApiGariWallet> {
        return try {
            val apiWallet = networkClient.get(
                gariClientId = gariClientId,
                token = token,
                path = Api.Path.WALLET_DETAILS,
                response = ApiGariWallet::class.java
            )
            Result.success(apiWallet)
        } catch (error: Throwable) {
            Result.failure(error)
        }
    }
}