package io.gari.sample.ui.wallet.details

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.coin.gari.domain.Gari
import io.coin.gari.domain.entity.GariWalletState
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
}