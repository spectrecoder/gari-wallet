package io.gari.sample.ui.wallet.transactions.send

sealed class TransactionViewState {

    object Ready : TransactionViewState()

    class Completed(val signature: String) : TransactionViewState()
}