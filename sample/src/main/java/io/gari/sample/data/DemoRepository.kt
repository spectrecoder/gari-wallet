package io.gari.sample.data

import io.gari.sample.network.service.DemoNetworkService

class DemoRepository(
    private val loginNetworkService: DemoNetworkService
) {

    suspend fun getWeb3AuthToken(userId: String): Result<String> {
        return loginNetworkService.getWeb3AuthToken(userId)
    }

    suspend fun sendTransaction(
        token: String,
        encodedTransaction: String
    ): Result<String> {
        return loginNetworkService.sendTransaction(token, encodedTransaction)
    }

    suspend fun refreshJwtToken(token: String): Result<String> {
        return loginNetworkService.refreshJwtToken(token)
    }
}