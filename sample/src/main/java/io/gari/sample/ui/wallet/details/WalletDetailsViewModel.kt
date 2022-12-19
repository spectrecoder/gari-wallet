package io.gari.sample.ui.wallet.details

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
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
    val publicKey = Transformations.map(walletState) {
        (it as? GariWalletState.Activated)?.pubKey
    }

    init {
        loadWalletDetails()
    }

    private fun loadWalletDetails() {
        viewModelScope.launch(Dispatchers.Default) {
            val state = Gari.getWalletState(web3AuthToken)
            walletState.postValue(state)
        }
    }

    fun registerWallet(keyManager: WalletKeyManager) {
        viewModelScope.launch(Dispatchers.Default) {
            Gari.createWallet(keyManager, web3AuthToken)
                .whenComplete { walletResult, error ->
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
                        walletState.postValue(GariWalletState.Activated(wallet.publicKey))
                    }
                }
        }
    }
}