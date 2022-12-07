package io.gari.sample.ui.wallet.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.coin.gari.domain.Gari
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class WalletDetailsViewModel(
    private val web3AuthToken: String
) : ViewModel() {

    init {
        viewModelScope.launch(Dispatchers.IO) {
            Gari.getWalletDetails(web3AuthToken)
        }
    }
}