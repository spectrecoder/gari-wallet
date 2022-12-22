package io.gari.sample.ui.wallet.transactions.history

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.gari.sample.data.DemoRepository

class TransactionsHistoryViewModel(
    private val web3AuthToken: String,
    private val demoRepository: DemoRepository
) : ViewModel() {

    val isProcessing = MutableLiveData(false)
}