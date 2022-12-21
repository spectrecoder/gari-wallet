package io.coin.gari.domain.usecase

import com.portto.solana.web3.KeyPair
import com.portto.solana.web3.SerializeConfig
import com.portto.solana.web3.Transaction
import io.coin.gari.data.GariWalletRepository
import io.coin.gari.domain.base64.Base64Util

internal class TransferGariTokenUseCase(
    private val gariWalletRepository: GariWalletRepository,
    private val base64Util: Base64Util
) {

    fun getEncodedTransaction(
        gariClientId: String,
        token: String,
        ownerPrivateKey: ByteArray,
        receiverPublicKey: String,
        transactionAmount: String
    ): Result<String> {
        val encodedTransactionResult = gariWalletRepository.getEncodedTransaction(
            gariClientId = gariClientId,
            token = token,
            receiverPublicKey = receiverPublicKey,
            transactionAmount = transactionAmount
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
        val signer = KeyPair.fromSecretKey(ownerPrivateKey)

        val transaction = Transaction.from(rawTransaction)
        transaction.partialSign(signer)

        val signedTransaction = transaction.serialize(
            config = SerializeConfig(
                requireAllSignatures = true,
                verifySignatures = false
            )
        )

        return Result.success(base64Util.toBase64(signedTransaction))
    }
}