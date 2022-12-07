package io.gari.sample.data

import io.gari.sample.network.service.LoginNetworkService

class LoginRepository(
    private val loginNetworkService: LoginNetworkService
) {

    suspend fun getWeb3AuthToken(userId: String): Result<String> {
        return loginNetworkService.getWeb3AuthToken(userId)
    }
}