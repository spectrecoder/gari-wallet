package io.gari.sample.di

import io.gari.sample.ui.wallet.details.WalletDetailsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val walletModule = module {

    viewModel { (web3AuthToken: String) ->
        WalletDetailsViewModel(
            web3AuthToken = web3AuthToken
        )
    }
}