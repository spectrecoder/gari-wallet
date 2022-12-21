package io.gari.sample.ui.wallet.transactions.send

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.gari.sample.R
import io.gari.sample.ui.login.InputError

class SendTransactionViewModel(
    private val web3AuthToken: String
) : ViewModel() {

    val receiverPublicKey = MutableLiveData<String>()
    val receiverPublicKeyError = MutableLiveData<InputError?>()

    val transactionAmount = MutableLiveData<String>()
    val transactionAmountError = MutableLiveData<InputError?>()

    fun sendTransaction() {
        val receiverPublicKey = receiverPublicKey.value

        if (receiverPublicKey.isNullOrEmpty()) {
            receiverPublicKeyError.value =
                InputError.EmptyField(R.string.send_transaction_missing_receiver)
            return
        }

        receiverPublicKeyError.value = null

        val transactionAmount = transactionAmount.value
        if (transactionAmount.isNullOrEmpty()) {
            transactionAmountError.value =
                InputError.EmptyField(R.string.send_transaction_missing_amount)
            return
        }

        transactionAmountError.value = null
    }
}