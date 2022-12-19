package io.coin.gari.domain.usecase

import com.portto.solana.web3.KeyPair
import com.portto.solana.web3.SerializeConfig
import com.portto.solana.web3.Transaction
import io.coin.gari.data.GariWalletRepository
import io.coin.gari.domain.base64.Base64Util

internal class RequestAirdropUseCase(
    private val gariWalletRepository: GariWalletRepository,
    private val base64Util: Base64Util
) {

    fun requestAirdrop(
        gariClientId: String,
        token: String,
        privateKey: ByteArray,
        airdropAmount: String
    ): Result<Unit> {
        val pubKey = try {
            KeyPair.fromSecretKey(privateKey).publicKey.toBase58()
        } catch (error: Throwable) {
            return Result.failure(error)
        }

        val encodedTransactionResult = gariWalletRepository.getEncodedAirdropTransaction(
            gariClientId = gariClientId,
            token = token,
            pubKey = pubKey,
            airdropAmount = airdropAmount
        )

        val encodedTransaction = encodedTransactionResult.getOrNull()

        if (encodedTransactionResult.isFailure
            || encodedTransaction.isNullOrEmpty()
        ) {
            return Result.failure(
                encodedTransactionResult.exceptionOrNull() ?: IllegalStateException()
            )
        }

        val rawTransaction = base64Util.fromBase64(encodedTransaction)
        val signer = KeyPair.fromSecretKey(privateKey)

        val transaction = Transaction.from(rawTransaction)
        transaction.partialSign(signer)

        val signedTransaction = transaction.serialize(
            config = SerializeConfig(
                requireAllSignatures = true,
                verifySignatures = false
            )
        )

        val encodedSignedTransaction = base64Util.toBase64(signedTransaction)

        return gariWalletRepository.sendSignedAirdropTransaction(
            gariClientId = gariClientId,
            token = token,
            pubKey = pubKey,
            airdropAmount = airdropAmount,
            encodedTransaction = encodedSignedTransaction
        )
    }
}