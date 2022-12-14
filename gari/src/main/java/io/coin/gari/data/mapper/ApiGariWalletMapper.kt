package io.coin.gari.data.mapper

import io.coin.gari.domain.entity.GariWallet
import io.coin.gari.network.entity.ApiGariWallet

internal class ApiGariWalletMapper : AbstractApiMapper<ApiGariWallet, GariWallet>() {

    override fun from(model: ApiGariWallet): GariWallet {
        val publicKey = requireNotEmpty(model.publicKey, model::publicKey)

        return GariWallet(
            publicKey = publicKey
        )
    }
}