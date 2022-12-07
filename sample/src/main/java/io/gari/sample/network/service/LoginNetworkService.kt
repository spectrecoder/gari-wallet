package io.gari.sample.network.service

class LoginNetworkService(
    private val loginApiService: LoginApiService
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
}