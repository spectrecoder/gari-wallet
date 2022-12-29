package io.coin.gari.domain

import androidx.activity.result.ActivityResultCaller
import io.coin.gari.di.NetworkModuleInjection
import io.coin.gari.di.UseCaseModuleInjection
import io.coin.gari.domain.entity.GariWallet
import io.coin.gari.domain.entity.GariWalletState
import io.coin.gari.domain.wallet.WalletKeyManager
import io.coin.gari.exceptions.Web3AuthorizeException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import kotlin.coroutines.resume

object Gari {

    private var clientId: String = ""

    private val getWalletDetailsUseCase = UseCaseModuleInjection.getWalletDetailsUseCase
    private val createWalletUseCase = UseCaseModuleInjection.createWalletUseCase
    private val requestAirdropUseCase = UseCaseModuleInjection.requestAirdropUseCase
    private val transferGariTokenUseCase = UseCaseModuleInjection.transferGariTokenUseCase

    fun initialize(clientId: String) {
        this.clientId = clientId
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
        keyManager: WalletKeyManager
    ): Result<ByteArray> {
        return suspendCancellableCoroutine<Result<ByteArray>> { continuation ->
            keyManager.getPrivateKey(
                token = token,
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