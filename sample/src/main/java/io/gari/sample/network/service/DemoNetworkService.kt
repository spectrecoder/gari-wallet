package io.gari.sample.network.service

import io.gari.sample.network.Api

class DemoNetworkService(
    private val loginApiService: DemoApiService
) {

    suspend fun getWeb3AuthToken(userId: String): Result<String> {
        try {
            val response = loginApiService.getWeb3AuthToken(userId)
            val web3AuthToken = response.body()

            if (!response.isSuccessful
                || web3AuthToken.isNullOrEmpty()
            ) {
                return Result.failure(IllegalStateException())
            }

            return Result.success(web3AuthToken)
        } catch (error: Throwable) {
            return Result.failure(error)
        }
    }

    suspend fun sendTransaction(
        token: String,
        transaction: String
    ): Result<String> {
        try {
            val params = hashMapOf(
                Api.Param.SIGNED_TRANSACTION to transaction
            )

            val response = loginApiService.sendTransaction(token, params)
            val web3AuthToken = response.body()

            if (!response.isSuccessful
                || web3AuthToken.isNullOrEmpty()
            ) {
                return Result.failure(IllegalStateException())
            }

            return Result.success(web3AuthToken)
        } catch (error: Throwable) {
            return Result.failure(error)
        }
    }
}