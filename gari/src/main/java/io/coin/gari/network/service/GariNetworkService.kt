package io.coin.gari.network.service

import io.coin.gari.exceptions.InvalidResponseBodyException
import io.coin.gari.exceptions.UserNotExistException
import io.coin.gari.network.Api
import io.coin.gari.network.core.NetworkClient
import io.coin.gari.network.entity.ApiGariWallet
import io.coin.gari.network.response.WalletDetailsResponse

internal class GariNetworkService(
    private val networkClient: NetworkClient
) {

    fun getWalletDetails(
        gariClientId: String,
        token: String
    ): Result<ApiGariWallet> {
        return try {
            val apiWalletResponse = networkClient.get(
                gariClientId = gariClientId,
                token = token,
                path = Api.Path.WALLET_DETAILS,
                responseClass = WalletDetailsResponse::class.java
            )

            val userExist = apiWalletResponse.userExist
                ?: throw InvalidResponseBodyException()

            if (!userExist) {
                throw UserNotExistException()
            }

            Result.success(ApiGariWallet())
        } catch (error: Throwable) {
            Result.failure(error)
        }
    }
}