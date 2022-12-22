package io.gari.sample.di

import io.gari.sample.ui.wallet.transactions.history.TransactionsHistoryViewModel
import io.gari.sample.ui.wallet.transactions.send.SendTransactionViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val transactionsModule = module {

    viewModel { (web3AuthToken: String) ->
        SendTransactionViewModel(
            web3AuthToken = web3AuthToken,
            demoRepository = get()
        )
    }

    viewModel { (web3AuthToken: String) ->
        TransactionsHistoryViewModel(
            web3AuthToken = web3AuthToken,
            demoRepository = get()
        )
    }
}