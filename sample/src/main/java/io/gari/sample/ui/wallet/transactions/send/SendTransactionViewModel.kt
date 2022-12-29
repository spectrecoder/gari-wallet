package io.gari.sample.ui.wallet.transactions.send

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.coin.gari.domain.Gari
import io.coin.gari.domain.wallet.WalletKeyManager
import io.coin.gari.utils.toLamports
import io.gari.sample.R
import io.gari.sample.data.DemoRepository
import io.gari.sample.ui.login.InputError
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SendTransactionViewModel(
    private val web3AuthToken: String,
    private val demoRepository: DemoRepository
) : ViewModel() {

    val receiverPublicKey = MutableLiveData<String>("")
    val receiverPublicKeyError = MutableLiveData<InputError?>()

    val transactionAmount = MutableLiveData<String>("0.5")
    val transactionAmountError = MutableLiveData<InputError?>()

    val viewState = MutableLiveData<TransactionViewState>(TransactionViewState.Ready)

    val isProcessing = MutableLiveData(false)

    fun sendTransaction(walletKeyManager: WalletKeyManager) {
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

        val amount = transactionAmount.toBigDecimalOrNull()
            ?.toLamports()
            ?.toString()
            ?: return

        isProcessing.value = true

        viewModelScope.launch(Dispatchers.IO) {
            val freshTokenResult = demoRepository.refreshJwtToken(web3AuthToken)
            val freshToken = freshTokenResult.getOrNull()

            if (freshTokenResult.isFailure
                || freshToken.isNullOrEmpty()
            ) {
                // todo: handle token failed
                isProcessing.postValue(false)
                return@launch
            }

            Gari.transferGariToken(
                token = freshToken,
                keyManager = walletKeyManager,
                receiverPublicKey = receiverPublicKey,
                transactionAmount = amount
            ).onSuccess { signedTransaction ->
                sendTransactionForValidation(
                    token = freshToken,
                    signedTransaction = signedTransaction
                )
            }.onFailure {
                isProcessing.postValue(false)
            }
        }
    }

    private fun sendTransactionForValidation(
        token: String,
        signedTransaction: String
    ) {
        viewModelScope.launch {
            demoRepository.sendTransaction(
                token = token,
                encodedTransaction = signedTransaction
            ).onSuccess { signature ->
                viewState.postValue(TransactionViewState.Completed(signature))
                isProcessing.postValue(false)
            }.onFailure {
                isProcessing.postValue(false)
            }
        }
    }
}