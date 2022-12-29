package io.gari.sample.di

import io.gari.sample.ui.wallet.airdrop.AirdropViewModel
import io.gari.sample.ui.wallet.details.WalletDetailsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val airdropModule = module {

    viewModel { (web3AuthToken: String, publicKey: String) ->
        AirdropViewModel(
            web3AuthToken = web3AuthToken,
            publicKey = publicKey
        )
    }
}