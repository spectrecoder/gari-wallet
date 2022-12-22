package io.gari.sample.ui.wallet.details

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.coin.gari.domain.Gari
import io.coin.gari.domain.entity.GariWalletState
import io.coin.gari.domain.wallet.WalletKeyManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class WalletDetailsViewModel(
    private val web3AuthToken: String
) : ViewModel() {

    val walletState = MutableLiveData<GariWalletState>()
    val isProcessing = MutableLiveData<Boolean>()

    init {
        loadWalletDetails()
    }

    private fun loadWalletDetails() {
        isProcessing.value = true
        viewModelScope.launch(Dispatchers.Default) {
            val state = Gari.getWalletState(web3AuthToken)
            walletState.postValue(state)
            isProcessing.postValue(false)
        }
    }

    fun reloadBalance() {
        isProcessing.value = true
        viewModelScope.launch(Dispatchers.Default) {
            val state = Gari.getWalletState(web3AuthToken)
            walletState.postValue(state)
            isProcessing.postValue(false)
        }
    }

    fun registerWallet(keyManager: WalletKeyManager) {
        viewModelScope.launch(Dispatchers.Default) {
            Gari.createWallet(
                keyManager = keyManager,
                token = web3AuthToken
            ).whenComplete { walletResult, error ->
                val wallet = walletResult.getOrNull()

                if (walletResult.isFailure
                    || wallet == null
                    || error != null
                ) {
                    walletState.postValue(
                        GariWalletState.Error(
                            walletResult.exceptionOrNull() ?: error
                        )
                    )
                } else {
                    walletState.postValue(
                        GariWalletState.Activated(
                            wallet.publicKey,
                            wallet.balance
                        )
                    )
                }
            }
        }
    }
}