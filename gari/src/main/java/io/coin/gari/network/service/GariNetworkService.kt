package io.coin.gari.network.service

import com.google.gson.reflect.TypeToken
import io.coin.gari.exceptions.InvalidResponseBodyException
import io.coin.gari.exceptions.UserNotExistException
import io.coin.gari.network.Api
import io.coin.gari.network.core.NetworkClient
import io.coin.gari.network.entity.ApiEncodedTransaction
import io.coin.gari.network.entity.ApiGariWallet
import io.coin.gari.network.response.GariResponse
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

    fun createWallet(
        gariClientId: String,
        token: String,
        pubKey: String
    ): Result<ApiGariWallet> {
        return try {
            val params = hashMapOf(
                Api.Param.PUBLIC_KEY to pubKey
            )

            val apiWalletResponse = networkClient.post(
                gariClientId = gariClientId,
                token = token,
                path = Api.Path.WALLET_CREATE,
                params = params,
                responseClass = WalletDetailsResponse::class.java
            )


            Result.success(ApiGariWallet())
        } catch (error: Throwable) {
            Result.failure(error)
        }
    }

    fun getEncodedAirdropTransaction(
        gariClientId: String,
        token: String,
        pubKey: String,
        airdropAmount: String
    ): Result<String> {
        return try {
            val params = hashMapOf(
                Api.Param.PUBLIC_KEY to pubKey,
                Api.Param.AIRDROP_AMOUNT to airdropAmount,
            )

            val response = networkClient.post<GariResponse<ApiEncodedTransaction>>(
                gariClientId = gariClientId,
                token = token,
                path = Api.Path.AIRDROP_GET_ENCODED_TRANSACTION,
                params = params,
                responseType = TypeToken.getParameterized(
                    GariResponse::class.java,
                    ApiEncodedTransaction::class.java
                ).type
            )

            val encodedTransaction = response.data?.encodedTransaction

            if (encodedTransaction.isNullOrEmpty()) {
                return Result.failure(InvalidResponseBodyException())
            }

            Result.success(encodedTransaction)
        } catch (error: Throwable) {
            Result.failure(error)
        }
    }

    fun sendSignedAirdropTransaction(
        gariClientId: String,
        token: String,
        pubKey: String,
        airdropAmount: String,
        encodedTransaction: String,
    ): Result<Unit> {
        return try {
            val params = hashMapOf(
                Api.Param.PUBLIC_KEY to pubKey,
                Api.Param.AIRDROP_AMOUNT to airdropAmount,
                Api.Param.ENCODED_TRANSACTION to encodedTransaction,
            )

            val response = networkClient.post<GariResponse<ApiEncodedTransaction>>(
                gariClientId = gariClientId,
                token = token,
                path = Api.Path.AIRDROP_SEND_SIGNED_TRANSACTION,
                params = params,
                responseType = TypeToken.getParameterized(
                    GariResponse::class.java,
                    ApiEncodedTransaction::class.java
                ).type
            )

            val encodedTransaction = response.data?.encodedTransaction

            if (encodedTransaction.isNullOrEmpty()) {
                return Result.failure(InvalidResponseBodyException())
            }

            Result.success(Unit)
        } catch (error: Throwable) {
            Result.failure(error)
        }
    }
}