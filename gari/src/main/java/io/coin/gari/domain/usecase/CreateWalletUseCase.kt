package io.coin.gari.domain.usecase

import com.portto.solana.web3.KeyPair
import io.coin.gari.data.GariWalletRepository
import io.coin.gari.domain.entity.GariWallet

internal class CreateWalletUseCase(
    private val gariWalletRepository: GariWalletRepository
) {

    fun createWallet(
        gariClientId: String,
        token: String,
        privateKey: ByteArray
    ): Result<GariWallet> {
        val pubKey = try {
            KeyPair.fromSecretKey(privateKey).publicKey.toBase58()
        } catch (error: Throwable) {
            return Result.failure(error)
        }

        return gariWalletRepository.createWallet(
            gariClientId = gariClientId,
            token = token,
            pubKey = pubKey
        )
    }
}