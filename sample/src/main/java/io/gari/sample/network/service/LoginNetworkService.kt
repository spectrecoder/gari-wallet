package io.gari.sample.network.service

class LoginNetworkService(
    private val loginApiService: LoginApiService
) {

    suspend fun getWeb3AuthToken(userId: String): Result<String> {
        try {
            val response = loginApiService.getWeb3AuthToken(userId)
            val responseBody = response.body()
            val web3AuthToken = responseBody?.token

            if (!response.isSuccessful
                || responseBody == null
                || web3AuthToken == null
            ) {
                return Result.failure(IllegalStateException())
            }

            return Result.success(web3AuthToken)
        } catch (error: Throwable) {
            return Result.failure(error)
        }
    }
}