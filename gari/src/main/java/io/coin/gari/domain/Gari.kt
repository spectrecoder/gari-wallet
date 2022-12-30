package io.coin.gari.domain

import androidx.activity.result.ActivityResultCaller
import io.coin.gari.di.NetworkModuleInjection
import io.coin.gari.di.UseCaseModuleInjection
import io.coin.gari.domain.entity.GariWallet
import io.coin.gari.domain.entity.GariWalletState
import io.coin.gari.domain.wallet.WalletKeyManager
import io.coin.gari.domain.web3.Web3AuthConfig
import io.coin.gari.exceptions.Web3AuthorizeException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import kotlin.coroutines.resume

object Gari {

    private var clientId: String = ""
    private var web3AuthConfig: Web3AuthConfig? = null

    private val getWalletDetailsUseCase = UseCaseModuleInjection.getWalletDetailsUseCase
    private val createWalletUseCase = UseCaseModuleInjection.createWalletUseCase
    private val requestAirdropUseCase = UseCaseModuleInjection.requestAirdropUseCase
    private val transferGariTokenUseCase = UseCaseModuleInjection.transferGariTokenUseCase

    fun initialize(clientId: String, web3AuthConfig: Web3AuthConfig) {
        this.clientId = clientId
        this.web3AuthConfig = web3AuthConfig
    }

    fun provideWalletKeyManager(resultCaller: ActivityResultCaller): WalletKeyManager {
        return WalletKeyManager(resultCaller)
    }

    fun setLogsEnabled(enable: Boolean) {
        NetworkModuleInjection.setLogsEnabled(enable)
    }

    suspend fun getWalletState(token: String): GariWalletState {
        return withContext(Dispatchers.IO) {
            getWalletDetailsUseCase.getWalletState(
                gariClientId = clientId,
                token = token
            )
        }
    }

    suspend fun createWallet(
        keyManager: WalletKeyManager,
        token: String
    ): Result<GariWallet> {
        val privateKeyResult = getPrivateKey(
            token,
            keyManager
        )

        val privateKey = privateKeyResult.getOrNull()

        if (privateKeyResult.isFailure
            || privateKey == null
        ) {
            return Result.failure(
                privateKeyResult.exceptionOrNull()
                    ?: Web3AuthorizeException()
            )
        }

        return withContext(Dispatchers.IO) {
            createWalletUseCase.createWallet(
                gariClientId = clientId,
                token = token,
                privateKey = privateKey
            )
        }
    }

    suspend fun getAirDrop(
        token: String,
        destinationPublicKey: String,
        sponsorPrivateKey: ByteArray,
        amount: String
    ): Result<String> {
        return withContext(Dispatchers.IO) {
            requestAirdropUseCase.requestAirdrop(
                gariClientId = clientId,
                token = token,
                airdropAmount = amount,
                destinationPublicKey = destinationPublicKey,
                sponsorPrivateKey = sponsorPrivateKey
            )
        }
    }

    /**
     * Method will return signed transaction encoded in base64
     * @param token - web3auth jwt token. Token should be refreshed before each function call {@link Gari#transferGariToken()}
     * @param keyManager - instance of WalletKeyManager, which is taken care about wallet keys,
     * check documentation to get more details
     * {@see <a href="https://github.com/gari-network/gari-wallet-android-sdk#-key-management">WalletKeyManager doc</a>}
     * @param receiverPublicKey - GARI public address to which it is planned to send tokens
     * @param transactionAmount - amount of gari in lamports. A lamport has a value of 0.000000001 GARI
     */
    suspend fun transferGariToken(
        token: String,
        keyManager: WalletKeyManager,
        receiverPublicKey: String,
        transactionAmount: String
    ): Result<String> {
        val privateKeyResult = getPrivateKey(
            token,
            keyManager
        )

        val privateKey = privateKeyResult.getOrNull()

        if (privateKeyResult.isFailure
            || privateKey == null
        ) {
            return Result.failure(
                privateKeyResult.exceptionOrNull()
                    ?: Web3AuthorizeException()
            )
        }

        return withContext(Dispatchers.IO) {
            transferGariTokenUseCase.getEncodedTransaction(
                gariClientId = clientId,
                token = token,
                ownerPrivateKey = privateKey,
                receiverPublicKey = receiverPublicKey,
                transactionAmount = transactionAmount
            )
        }
    }

    private suspend fun getPrivateKey(
        token: String,
        keyManager: WalletKeyManager,
    ): Result<ByteArray> {
        val web3AuthConfig = web3AuthConfig
            ?: return Result.failure(
                IllegalStateException("Web3AuthConfig is not initialized. Check Gari.initialize() function")
            )

        return suspendCancellableCoroutine<Result<ByteArray>> { continuation ->
            keyManager.getPrivateKey(
                token = token,
                web3AuthConfig = web3AuthConfig,
                onSuccess = { key ->
                    if (!continuation.isCancelled) {
                        continuation.resume(Result.success(key))
                    }
                },
                onFailure = {
                    if (!continuation.isCancelled) {
                        continuation.resume(Result.failure(Web3AuthorizeException()))
                    }
                }
            )
        }
    }
}